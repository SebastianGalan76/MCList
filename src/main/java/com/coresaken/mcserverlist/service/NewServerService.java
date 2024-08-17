package com.coresaken.mcserverlist.service;

import com.coresaken.mcserverlist.data.dto.BasicServerDto;
import com.coresaken.mcserverlist.data.response.Response;
import com.coresaken.mcserverlist.data.dto.ServerStatusDto;
import com.coresaken.mcserverlist.database.model.server.*;
import com.coresaken.mcserverlist.database.repository.*;
import com.coresaken.mcserverlist.database.repository.server.DailyPlayerCountRepository;
import com.coresaken.mcserverlist.database.repository.server.ModeRepository;
import com.coresaken.mcserverlist.util.UnicodeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewServerService {
    final ServerStatusService serverStatusService;

    final ServerRepository serverRepository;
    final SubServerRepository subServerRepository;

    final ServerDetailRepository serverDetailRepository;
    final NameRepository nameRepository;
    final ModeRepository modeRepository;
    final BlockedServerRepository blockedServerRepository;

    final HourlyPlayerCountRepository hourlyPlayerCountRepository;
    final DailyPlayerCountRepository dailyPlayerCountRepository;

    final PlayerStatsService playerStatsService;

    @Transactional
    public Response addNewServer(BasicServerDto basicServerDto){
        Response response = checkServerData(basicServerDto, null);
        if(response.getStatus() != HttpStatus.OK){
            return response;
        }

        ServerStatusDto serverStatusDto = serverStatusService.getServerStatus(basicServerDto.getIp(), basicServerDto.getPort());
        if(!serverStatusDto.online()){
            return Response.builder().status(HttpStatus.NOT_FOUND).build();
        }

        Server server = new Server();
        server = serverRepository.save(server);
        saveBasicInformation(server, basicServerDto, serverStatusDto);

        HourlyPlayerCount hourlyPlayerCount = new HourlyPlayerCount();
        hourlyPlayerCount.setTime(LocalDateTime.now());
        hourlyPlayerCount.setPlayerCount(serverStatusDto.players().online());
        hourlyPlayerCount.setServer(server);
        hourlyPlayerCountRepository.save(hourlyPlayerCount);

        DailyPlayerCount dailyPlayerCount = new DailyPlayerCount();
        dailyPlayerCount.setTime(LocalDateTime.now());
        dailyPlayerCount.setPlayerCount(serverStatusDto.players().online());
        dailyPlayerCount.setServer(server);
        dailyPlayerCountRepository.save(dailyPlayerCount);

        List<Mode> modes = basicServerDto.getModes();
        if(modes != null && !modes.isEmpty()){
            if(modes.size() == 1){
                server.setMode(modes.get(0));
            }
            else{
                server.setMode(modeRepository.getReferenceById(1L));
                int index = 0;
                for(Mode mode:modes){
                    if(mode.getId() == 1){
                        continue;
                    }

                    SubServer subServer = new SubServer();
                    subServer.setMode(mode);
                    subServer.setName(nameRepository.save(new Name(mode.getName(), "#ffffff")));
                    subServer.setVersions(basicServerDto.getVersions());
                    subServer.setIndex(index);
                    subServer.setServer(server);

                    subServerRepository.save(subServer);
                    if(server.getSubServers() == null){
                        server.setSubServers(new ArrayList<>());
                    }
                    server.getSubServers().add(subServer);
                    index++;
                }
            }
        }
        LocalDateTime now = LocalDateTime.now();
        server.setNextRefreshAt(now.plusMinutes(PlayerStatsService.REFRESH_INTERVAL_MINUTE));
        server.setCreatedAt(now);

        serverRepository.save(server);

        return Response.builder().status(HttpStatus.PERMANENT_REDIRECT).redirect("/server/"+server.getIp()).build();
    }

    public void saveBasicInformation(Server server, BasicServerDto basicServerDto, ServerStatusDto serverStatusDto){
        Name name = server.getName();
        if(name != null){
            name.setName(basicServerDto.getIp());
            nameRepository.save(name);
        }
        else{
            server.setName(nameRepository.save(new Name(basicServerDto.getIp(), "#ffffff")));
        }

        server.setIp(basicServerDto.getIp().toLowerCase());
        server.setPort(basicServerDto.getPort());
        server.setOnline(serverStatusDto.online());
        server.setOnlinePlayers(serverStatusDto.players().online());
        server.setPremium(basicServerDto.isPremium());
        server.setMods(basicServerDto.isMods());

        ServerDetail serverDetail = new ServerDetail();
        serverDetail.setMotdHtml(serverStatusDto.motd().html());
        serverDetail.setMotdClean(UnicodeConverter.convertUnicodeToAscii(serverStatusDto.motd().clean()));
        serverDetail.setIcon(serverStatusDto.icon());
        serverDetail = serverDetailRepository.save(serverDetail);
        server.setDetail(serverDetail);

        if(server.getVersions() == null){
            server.setVersions(basicServerDto.getVersions());
        }
        else{
            server.getVersions().clear();
            server.getVersions().addAll(basicServerDto.getVersions());
        }

        List<Mode> modes = basicServerDto.getModes();
        if(modes != null && !modes.isEmpty()){
            if(modes.size()==1){
                server.setMode(modes.get(0));
            }
            else{
                server.setMode(modeRepository.getReferenceById(1L));
            }
        }
        else{
            server.setMode(null);
        }

        if(server.getSubServers() != null && server.getSubServers().size()>1){
            server.setMode(modeRepository.getReferenceById(1L));
        }
    }
    public Response checkServerData(BasicServerDto serverDto, Server server){
        if(serverDto.getIp().contains("aternos.me")){
            return Response.builder().status(HttpStatus.BAD_REQUEST).message("Nie możesz dodawać serwera aternos!").build();
        }

        BlockedServer blockedServer = blockedServerRepository.findByIp(serverDto.getIp()).orElse(null);
        if(blockedServer!=null){
            if(blockedServer.getServer()!=null){
                return Response.builder().status(HttpStatus.PERMANENT_REDIRECT).redirect("/server/"+ serverDto.getIp()).build();
            }
            else {
                return Response.builder().status(HttpStatus.BAD_REQUEST).message("Ten adres IP jest obecnie zablokowany! Jeśli uważasz to za błąd, skontaktuj się z nami.").build();
            }
        }

        if(serverRepository.findByIp(serverDto.getIp()).isPresent()){
            if(server == null){
                return Response.builder().status(HttpStatus.BAD_REQUEST).message("Serwer znajduje się już na liście.").build();
            }
            else{
                if(!server.getIp().equalsIgnoreCase(serverDto.getIp())){
                    return Response.builder().status(HttpStatus.BAD_REQUEST).message("Serwer znajduje się już na liście.").build();
                }
            }
        }

        return Response.builder().status(HttpStatus.OK).build();
    }
}
