package com.coresaken.mcserverlist.service;

import com.coresaken.mcserverlist.data.response.Response;
import com.coresaken.mcserverlist.database.model.Banner;
import com.coresaken.mcserverlist.database.model.User;
import com.coresaken.mcserverlist.database.repository.BannerRepository;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class BannerService {
    final UserService userService;
    final BannerRepository bannerRepository;

    private List<Banner> bigBanners;
    private int bigBannerIndex;

    private List<Banner> smallBanners;
    private int smallBannerIndex;

    @PostConstruct
    public void initialize(){
        bigBanners = bannerRepository.findByStatusAndSize(Banner.Status.PUBLISHED, Banner.Size.BIG);
        smallBanners = bannerRepository.findByStatusAndSize(Banner.Status.PUBLISHED, Banner.Size.SMALL);

        bigBannerIndex = 0;
        smallBannerIndex = 0;
    }

    public Banner getBigBanner(){
        if(bigBanners.isEmpty()){
            return null;
        }
        return bigBanners.get(bigBannerIndex++%bigBanners.size());
    }

    public List<Banner> getSmallBanners(){
        if(smallBanners.isEmpty()){
            return null;
        }

        List<Banner> banners = new ArrayList<>();
        for(int i=0;i<3;i++){
            banners.add(smallBanners.get(smallBannerIndex++%smallBanners.size()));
        }

        return banners;
    }

    public Response createBanner(MultipartFile file, String link, String size) {
        User user = userService.getLoggedUser();
        if(user == null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Twoja sesja wygasła. Zaloguj się ponownie").build();
        }

        Response uploadResponse = BannerFileService.upload(file);
        if(uploadResponse.getStatus() != HttpStatus.OK){
            return uploadResponse;
        }

        Banner banner = new Banner();
        banner.setSize(Banner.Size.valueOf(size));
        banner.setLink(link);
        banner.setOwner(user);
        banner.changeStatus(Banner.Status.NOT_VERIFIED, null);
        banner.setFilePath(uploadResponse.getMessage());
        bannerRepository.save(banner);

        return Response.builder().status(HttpStatus.OK).message("Banner został przesłany do weryfikacji. Przejdź do zakładki \"Moje Banery\", aby wyświetlić status").build();
    }

    public Response acceptBanner(Long bannerId){
        Banner banner = bannerRepository.findById(bannerId).orElse(null);
        if(banner == null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Wystąpił błąd #8725. Brak baneru o podanym ID").build();
        }

        User user = userService.getLoggedUser();
        if(user==null || user.getRole() != User.Role.ADMIN){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Nie posiadasz wymaganych uprawnień, aby to zrobić").build();
        }

        if(banner.isPaid()){
            banner.changeStatus(Banner.Status.PUBLISHED, null);

            if(banner.getSize() == Banner.Size.BIG){
                bigBanners.add(banner);
            }
            else if(banner.getSize() == Banner.Size.SMALL){
                smallBanners.add(banner);
            }
        }
        else{
            banner.changeStatus(Banner.Status.ACCEPTED, null);
        }

        bannerRepository.save(banner);
        return Response.builder().status(HttpStatus.OK).message("Baner został zaakceptowany").build();
    }

    public Response rejectBanner(Long bannerId, String reason){
        Banner banner = bannerRepository.findById(bannerId).orElse(null);
        if(banner == null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Wystąpił błąd #8725. Brak baneru o podanym ID").build();
        }

        User user = userService.getLoggedUser();
        if(user==null || user.getRole() != User.Role.ADMIN){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Nie posiadasz wymaganych uprawnień, aby to zrobić").build();
        }

        banner.changeStatus(Banner.Status.REJECTED, reason);
        bannerRepository.save(banner);
        return Response.builder().status(HttpStatus.OK).message("Baner został odrzucony").build();
    }

    public void publishBanner(Banner banner){
        banner.changeStatus(Banner.Status.PUBLISHED, null);

        LocalDateTime now = LocalDateTime.now();
        banner.setPublishedAt(now);
        banner.setFinishedAt(now.plusDays(31));
        banner.setPaid(true);

        if(banner.getSize() == Banner.Size.BIG){
            bigBanners.add(banner);
        }
        else if(banner.getSize() == Banner.Size.SMALL){
            smallBanners.add(banner);
        }

        bannerRepository.save(banner);
    }

    public Response editBanner(Long id, MultipartFile file, String link) {
        Banner banner = bannerRepository.findById(id).orElse(null);
        if(banner == null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Wystąpił błąd #8725. Brak baneru o podanym ID").build();
        }

        User user = userService.getLoggedUser();
        if(user==null || user.getRole() != User.Role.ADMIN || !banner.getOwner().equals(user)){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Nie posiadasz wymaganych uprawnień, aby to zrobić").build();
        }

        if(banner.getSize() == Banner.Size.BIG){
            bigBanners.remove(banner);
        }
        else if(banner.getSize() == Banner.Size.SMALL){
            smallBanners.remove(banner);
        }

        if(file != null){
            Response uploadResponse = BannerFileService.upload(file);
            if(uploadResponse.getStatus() != HttpStatus.OK){
                return uploadResponse;
            }

            BannerFileService.remove(banner.getFilePath());
            banner.setFilePath(uploadResponse.getMessage());
        }
        banner.setLink(link);
        banner.changeStatus(Banner.Status.NOT_VERIFIED, null);
        bannerRepository.save(banner);
        if(banner.isPaid()){
            return Response.builder().status(HttpStatus.OK).message("Zmiany zostały wysłane do weryfikacji. Po weryfikacji baner zostanie automatycznie opublikowany").build();
        }
        return Response.builder().status(HttpStatus.OK).message("Zmiany zostały wysłane do weryfikacji").build();
    }

    public Response deleteBanner(Long id) {
        Banner banner = bannerRepository.findById(id).orElse(null);
        if(banner == null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Wystąpił błąd #8725. Brak baneru o podanym ID").build();
        }

        if(banner.getSize() == Banner.Size.BIG){
            bigBanners.remove(banner);
        }
        else if(banner.getSize() == Banner.Size.SMALL){
            smallBanners.remove(banner);
        }

        User user = userService.getLoggedUser();
        if(user==null || user.getRole() != User.Role.ADMIN || !banner.getOwner().equals(user)){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Nie posiadasz wymaganych uprawnień, aby to zrobić").build();
        }

        BannerFileService.remove(banner.getFilePath());
        bannerRepository.delete(banner);
        return Response.builder().status(HttpStatus.OK).build();
    }

    public List<Banner> getBannersByStatus(Banner.Status[] statuses){
        List<Banner> banners = new ArrayList<>();
        for(Banner.Status status:statuses){
            banners.addAll(bannerRepository.findByStatus(status));
        }
        return banners;
    }

    public void active(com.coresaken.mcserverlist.data.payment.Banner bannerDto) {
        Banner banner = bannerRepository.findById(bannerDto.getBannerId()).orElse(null);
        if(banner==null){
            return;
        }

        publishBanner(banner);
    }

    @Nullable
    public Banner getById(Long id) {
        return bannerRepository.findById(id).orElse(null);
    }
}
