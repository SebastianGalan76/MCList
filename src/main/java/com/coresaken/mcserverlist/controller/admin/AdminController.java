package com.coresaken.mcserverlist.controller.admin;

import com.coresaken.mcserverlist.data.response.Response;
import com.coresaken.mcserverlist.database.model.Banner;
import com.coresaken.mcserverlist.database.model.User;
import com.coresaken.mcserverlist.service.BannerService;
import com.coresaken.mcserverlist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {
    final BannerService bannerService;
    final UserService userService;

    @RequestMapping("/admin/banner")
    public String getBannersPage(Model model){
        User user = userService.getLoggedUser();
        if(user == null || user.getRole() != User.Role.ADMIN){
            return "auth/signIn";
        }

        List<Banner> banners = bannerService.getBannersByStatus(new Banner.Status[] {Banner.Status.PUBLISHED, Banner.Status.NOT_VERIFIED});
        model.addAttribute("user", user);
        model.addAttribute("banners", banners);

        return "user/admin/manageBanner";
    }
}
