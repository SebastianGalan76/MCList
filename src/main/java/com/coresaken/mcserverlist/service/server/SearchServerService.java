package com.coresaken.mcserverlist.service.server;

import com.coresaken.mcserverlist.data.dto.SearchServerDto;
import com.coresaken.mcserverlist.database.model.server.Server;
import com.coresaken.mcserverlist.database.repository.ServerRepository;
import com.coresaken.mcserverlist.database.repository.SubServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServerService {
    final ServerRepository serverRepository;
    final SubServerRepository subServerRepository;

    public Page<Server> searchServer(SearchServerDto searchServerDto, Pageable pageable){
        Set<Server> serversByName = new HashSet<>();
        if(searchServerDto.getName() != null && !searchServerDto.getName().isEmpty()){
            serversByName.addAll(serverRepository.searchByIp(searchServerDto.getName()));
            serversByName.addAll(serverRepository.searchByMotd(searchServerDto.getName()));
        }
        else{
            serversByName.addAll(serverRepository.findAll());
        }

        Long versionId = searchServerDto.getVersion() != null ? searchServerDto.getVersion().getId() : 0;
        Set<Server> serversByModeAndVersions = new HashSet<>(serverRepository.findServersByModeAndVersionRange(searchServerDto.getMode(), versionId));
        serversByModeAndVersions.addAll(subServerRepository.findServersByModeAndVersion(searchServerDto.getMode(), versionId));

        Set<Server> commonServers = new HashSet<>(serversByName);
        if (searchServerDto.getMode() != null || searchServerDto.getVersion() != null) {
            commonServers.retainAll(serversByModeAndVersions);
        }
        if (searchServerDto.isPremium()) {
            commonServers.retainAll(serverRepository.findAllPremiumServers());
        }
        if(searchServerDto.isMods()){
            commonServers.retainAll(serverRepository.findAllServersWithMods());
        }

        List<Server> resultList = new ArrayList<>(commonServers);
        resultList = resultList.stream()
                .sorted(Comparator
                        .comparingInt(Server::getPromotionPoints)
                        .thenComparingInt(s -> s.getVotes().size()).reversed())
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), resultList.size());

        return new PageImpl<>(resultList.subList(start, end), pageable, resultList.size());
    }
}
