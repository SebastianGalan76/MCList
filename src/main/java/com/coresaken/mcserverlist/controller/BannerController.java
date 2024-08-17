package com.coresaken.mcserverlist.controller;

import com.coresaken.mcserverlist.data.response.Response;
import com.coresaken.mcserverlist.service.BannerService;
import com.coresaken.mcserverlist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class BannerController {
    final UserService userService;
    final BannerService bannerService;

    @RequestMapping("/banners")
    public String getBannerPage(Model model){
        model.addAttribute("user", userService.getLoggedUser());
        return "subPage/banner";
    }

    @ResponseBody
    @PostMapping("/banner")
    public Response createBanner(@Param("file") MultipartFile file, @Param("url") String link, @Param("size") String size){
        return bannerService.createBanner(file, link, size);
    }

    @ResponseBody
    @PutMapping("/banner/{id}")
    public Response editBanner(@PathVariable("id") Long id, @Param("file") MultipartFile file, @Param("url") String link){
        return bannerService.editBanner(id, file, link);
    }

    @ResponseBody
    @DeleteMapping("/banner/{id}")
    public Response deleteBanner(@PathVariable("id") Long id){
        return bannerService.deleteBanner(id);
    }

    @ResponseBody
    @PostMapping("/banner/{id}/accept")
    public Response acceptBanner(@PathVariable("id") Long id){
        return bannerService.acceptBanner(id);
    }

    @ResponseBody
    @PostMapping("/banner/{id}/reject")
    public Response rejectBanner(@PathVariable("id") Long id, @Param("reason") String reason){
        return bannerService.rejectBanner(id, reason);
    }
}
