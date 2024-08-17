package com.coresaken.mcserverlist.service.server;

import com.coresaken.mcserverlist.data.response.Response;
import com.coresaken.mcserverlist.database.model.User;
import com.coresaken.mcserverlist.database.model.server.Report;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.database.repository.server.ReportRepository;
import com.coresaken.mcserverlist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServerService {
    final ServerService serverService;
    final UserService userService;

    final ReportRepository reportRepository;

    public Response reportServer(Long id, String reason) {
        Server server = serverService.getServerById(id);

        if(server==null){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Wystąpił nieoczekiwany błąd #323245").build();
        }

        User user = userService.getLoggedUser();
        if(user == null){
            return Response.builder().status(HttpStatus.UNAUTHORIZED).message("Twoja sesja wygasła. Musisz się zalogować ponownie").build();
        }

        Report report = new Report();
        report.setReason(reason);
        report.setUser(user);
        report.setServer(server);
        reportRepository.save(report);
        return Response.builder().status(HttpStatus.OK).message("Zgłoszenie zostało wysłane do administracji").build();
    }
}
