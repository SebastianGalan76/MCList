package com.coresaken.mcserverlist.controller;

import com.coresaken.mcserverlist.data.payment.Banner;
import com.coresaken.mcserverlist.data.payment.PaymentAction;
import com.coresaken.mcserverlist.data.payment.PaymentDto;
import com.coresaken.mcserverlist.data.payment.PromotionPoints;
import com.coresaken.mcserverlist.database.model.Payment;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.database.repository.PaymentRepository;
import com.coresaken.mcserverlist.service.BannerService;
import com.coresaken.mcserverlist.service.PaymentService;
import com.coresaken.mcserverlist.service.PromotionPointService;
import com.coresaken.mcserverlist.service.server.ServerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PaymentController {

    final PaymentService paymentService;
    final PaymentRepository paymentRepository;

    final ServerService serverService;
    final BannerService bannerService;

    final PromotionPointService promotionPointService;

    @Value("${hotpay.secret}")
    String secret;
    @Value("${hotpay.password}")
    String password;
    @Value("${website.address}")
    String websiteAddress;

    @RequestMapping("/server/payment/{ip}/{promotionPoints}")
    public String getPaymentPage(@PathVariable("ip") String ip,
                                 @PathVariable("promotionPoints") int promotionPointsAmount,
                                 Model model) {
        Server server = serverService.getServerByIp(ip);
        if(server == null){
            return "error-404";
        }

        if(promotionPointsAmount < 10 || promotionPointsAmount > 365){
            return "error-404";
        }

        PromotionPoints promotionPoints = new PromotionPoints();
        promotionPoints.setServerId(server.getId());
        promotionPoints.setPromotionPoints(promotionPointsAmount);
        String successAction;
        try{
            ObjectMapper mapper = new ObjectMapper();
            successAction = mapper.writeValueAsString(promotionPoints);
        }catch (JsonProcessingException e) {
            return "error-404";
        }

        Payment payment = new Payment();
        payment.setAction(PaymentAction.PROMOTION_POINTS);
        payment.setServiceId(paymentService.generateRandomServiceId());
        payment.setStatus(Payment.Status.DEFAULT);
        payment.setSuccessAction(successAction);
        payment = paymentRepository.save(payment);
        int price = promotionPointsAmount * 2;

        PaymentDto paymentDto = getPromotionPaymentDto(ip, payment.getId(), String.valueOf(price));
        model.addAttribute("paymentRequest", paymentDto);

        return "subPage/payment";
    }

    @RequestMapping("/banner/payment/{id}")
    public String getBannerPaymentPage(@PathVariable("id") Long id,
                                 Model model) {
        com.coresaken.mcserverlist.database.model.Banner banner = bannerService.getById(id);
        if(banner == null){
            return "error-404";
        }

        Banner bannerPayment = new Banner();
        bannerPayment.setBannerId(id);

        String successAction;
        try{
            ObjectMapper mapper = new ObjectMapper();
            successAction = mapper.writeValueAsString(bannerPayment);
        }catch (JsonProcessingException e) {
            return "error-404";
        }

        Payment payment = new Payment();
        payment.setAction(PaymentAction.BANNER);
        payment.setServiceId(paymentService.generateRandomServiceId());
        payment.setStatus(Payment.Status.DEFAULT);
        payment.setSuccessAction(successAction);
        payment = paymentRepository.save(payment);
        String price;
        if(banner.getSize()== com.coresaken.mcserverlist.database.model.Banner.Size.BIG){
            price = "100";
        }
        else {
            price = "30";
        }

        PaymentDto paymentDto = getBannerPaymentDto(payment.getId(), price);
        model.addAttribute("paymentRequest", paymentDto);

        return "subPage/payment";
    }

    @ResponseBody
    @PostMapping("/payment-notification")
    public String getPaymentNotification(
            @RequestParam(required = false) String ID_ZAMOWIENIA,
            @RequestParam(required = false) String KWOTA,
            @RequestParam(required = false) String ID_PLATNOSCI,
            @RequestParam(required = false) String STATUS,
            @RequestParam(required = false) String SECURE,
            @RequestParam(required = false) String SEKRET,
            @RequestParam(required = false) String HASH) {

        // Obsługa parametrów
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(password).append(";");
        if (KWOTA != null) {
            stringBuilder.append(KWOTA).append(";");
        }
        if (ID_PLATNOSCI != null) {
            stringBuilder.append(ID_PLATNOSCI).append(";");
        }
        if (ID_ZAMOWIENIA != null) {
            stringBuilder.append(ID_ZAMOWIENIA).append(";");
        }
        if (STATUS != null) {
            stringBuilder.append(STATUS).append(";");
        }
        if (SECURE != null) {
            stringBuilder.append(SECURE).append(";");
        }
        if (SEKRET != null) {
            stringBuilder.append(SEKRET);
        }

        String computedHash = paymentService.hashSHA256(stringBuilder.toString());

        if (HASH == null || !HASH.equals(computedHash)) {
            return "Hashes do not match!";
        }

        Payment payment = paymentRepository.findById(Long.parseLong(ID_ZAMOWIENIA)).orElse(null);
        if(payment == null){
            return "Wystąpił nieoczekiwany błąd #3241622!";
        }
        payment.setPaymentId(ID_PLATNOSCI);

        Payment.Status status = Payment.Status.valueOf(STATUS);
        payment.setStatus(status);
        paymentRepository.save(payment);

        if(status != Payment.Status.SUCCESS){
            return STATUS;
        }

        if(payment.getAction() == PaymentAction.PROMOTION_POINTS){
            try{
                ObjectMapper mapper = new ObjectMapper();
                PromotionPoints promotionPoints = mapper.readValue(payment.getSuccessAction(), PromotionPoints.class);

                promotionPointService.addPromotionPoints(promotionPoints);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        else if(payment.getAction() == PaymentAction.BANNER){
            try{
                ObjectMapper mapper = new ObjectMapper();
                Banner banner = mapper.readValue(payment.getSuccessAction(), Banner.class);

               bannerService.active(banner);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return STATUS;
    }

    private PaymentDto getPromotionPaymentDto(String ip, Long orderId, String price){
        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setSecret(secret);
        paymentDto.setAmount(price);
        paymentDto.setServiceName("Punkty promowania dla serwera: " + ip);
        paymentDto.setWebsiteAddress(websiteAddress);
        paymentDto.setOrderId(String.valueOf(orderId));

        String stringBuilder = password + ";" +
                paymentDto.getAmount() + ";" +
                paymentDto.getServiceName() + ";" +
                paymentDto.getWebsiteAddress() + ";" +
                paymentDto.getOrderId() + ";" +
                paymentDto.getSecret();
        String hash = paymentService.hashSHA256(stringBuilder);
        paymentDto.setHash(hash);

        return paymentDto;
    }

    private PaymentDto getBannerPaymentDto(Long orderId, String price){
        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setSecret(secret);
        paymentDto.setAmount(price);
        paymentDto.setServiceName("Banner reklamowy");
        paymentDto.setWebsiteAddress(websiteAddress);
        paymentDto.setOrderId(String.valueOf(orderId));

        String stringBuilder = password + ";" +
                paymentDto.getAmount() + ";" +
                paymentDto.getServiceName() + ";" +
                paymentDto.getWebsiteAddress() + ";" +
                paymentDto.getOrderId() + ";" +
                paymentDto.getSecret();
        String hash = paymentService.hashSHA256(stringBuilder);
        paymentDto.setHash(hash);

        return paymentDto;
    }
}
