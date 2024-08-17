package com.coresaken.mcserverlist.database;

import com.coresaken.mcserverlist.database.model.server.Mode;
import com.coresaken.mcserverlist.database.model.server.Version;
import com.coresaken.mcserverlist.database.model.server.ratings.RatingCategory;
import com.coresaken.mcserverlist.database.repository.RatingCategoryRepository;
import com.coresaken.mcserverlist.database.repository.server.ModeRepository;
import com.coresaken.mcserverlist.database.repository.server.VersionRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@RequiredArgsConstructor
@Order(1)
public class DefaultDataLoader implements CommandLineRunner {
    final VersionRepository versionRepository;
    final ModeRepository modeRepository;
    final RatingCategoryRepository ratingCategoryRepository;

    @Override
    public void run(String... args) throws Exception {
        loadDefaultData();
    }

    private void loadDefaultData(){
        saveVersions();
        saveModes();
        saveRatingCategories();
    }

    private void saveVersions(){
        List<Version> versions = new ArrayList<>();
        versions.add(new Version(1211L, "1.21.1"));
        versions.add(new Version(1210L, "1.21"));
        versions.add(new Version(1206L, "1.20.6"));
        versions.add(new Version(1205L, "1.20.5"));
        versions.add(new Version(1204L, "1.20.4"));
        versions.add(new Version(1203L, "1.20.3"));
        versions.add(new Version(1202L, "1.20.2"));
        versions.add(new Version(1201L, "1.20.1"));
        versions.add(new Version(1200L, "1.20"));
        versions.add(new Version(1194L, "1.19.4"));
        versions.add(new Version(1193L, "1.19.3"));
        versions.add(new Version(1192L, "1.19.2"));
        versions.add(new Version(1191L, "1.19.1"));
        versions.add(new Version(1190L, "1.19"));
        versions.add(new Version(1182L, "1.18.2"));
        versions.add(new Version(1181L, "1.18.1"));
        versions.add(new Version(1180L, "1.18"));
        versions.add(new Version(1171L, "1.17.1"));
        versions.add(new Version(1170L, "1.17"));
        versions.add(new Version(1165L, "1.16.5"));
        versions.add(new Version(1164L, "1.16.4"));
        versions.add(new Version(1163L, "1.16.3"));
        versions.add(new Version(1162L, "1.16.2"));
        versions.add(new Version(1161L, "1.16.1"));
        versions.add(new Version(1160L, "1.16"));
        versions.add(new Version(1152L, "1.15.2"));
        versions.add(new Version(1151L, "1.15.1"));
        versions.add(new Version(1150L, "1.15"));
        versions.add(new Version(1144L, "1.14.4"));
        versions.add(new Version(1143L, "1.14.3"));
        versions.add(new Version(1142L, "1.14.2"));
        versions.add(new Version(1141L, "1.14.1"));
        versions.add(new Version(1140L, "1.14"));
        versions.add(new Version(1132L, "1.13.2"));
        versions.add(new Version(1131L, "1.13.1"));
        versions.add(new Version(1130L, "1.13"));
        versions.add(new Version(1122L, "1.12.2"));
        versions.add(new Version(1121L, "1.12.1"));
        versions.add(new Version(1120L, "1.12"));
        versions.add(new Version(1112L, "1.11.2"));
        versions.add(new Version(1111L, "1.11.1"));
        versions.add(new Version(1110L, "1.11"));
        versions.add(new Version(1102L, "1.10.2"));
        versions.add(new Version(1101L, "1.10.1"));
        versions.add(new Version(1100L, "1.10"));
        versions.add(new Version(1094L, "1.9.4"));
        versions.add(new Version(1093L, "1.9.3"));
        versions.add(new Version(1092L, "1.9.2"));
        versions.add(new Version(1091L, "1.9.1"));
        versions.add(new Version(1090L, "1.9"));
        versions.add(new Version(1089L, "1.8.9"));
        versions.add(new Version(1088L, "1.8.8"));
        versions.add(new Version(1087L, "1.8.7"));
        versions.add(new Version(1086L, "1.8.6"));
        versions.add(new Version(1085L, "1.8.5"));
        versions.add(new Version(1084L, "1.8.4"));
        versions.add(new Version(1083L, "1.8.3"));
        versions.add(new Version(1082L, "1.8.2"));
        versions.add(new Version(1081L, "1.8.1"));
        versions.add(new Version(1080L, "1.8"));
        versions.add(new Version(1079L, "1.7.9"));
        versions.add(new Version(1078L, "1.7.8"));
        versions.add(new Version(1077L, "1.7.7"));
        versions.add(new Version(1076L, "1.7.6"));
        versions.add(new Version(1075L, "1.7.5"));
        versions.add(new Version(1074L, "1.7.4"));
        versions.add(new Version(1073L, "1.7.3"));
        versions.add(new Version(1072L, "1.7.2"));
        versions.add(new Version(1071L, "1.7.1"));
        versions.add(new Version(1070L, "1.7"));
        versions.add(new Version(1064L, "1.6.4"));
        versions.add(new Version(1063L, "1.6.3"));
        versions.add(new Version(1062L, "1.6.2"));
        versions.add(new Version(1061L, "1.6.1"));
        versions.add(new Version(1060L, "1.6"));
        versions.add(new Version(1052L, "1.5.2"));
        versions.add(new Version(1051L, "1.5.1"));
        versions.add(new Version(1050L, "1.5"));
        versions.add(new Version(1047L, "1.4.7"));
        versions.add(new Version(1046L, "1.4.6"));
        versions.add(new Version(1045L, "1.4.5"));
        versions.add(new Version(1044L, "1.4.4"));
        versions.add(new Version(1043L, "1.4.3"));
        versions.add(new Version(1042L, "1.4.2"));
        versions.add(new Version(1041L, "1.4.1"));
        versions.add(new Version(1040L, "1.4"));
        versions.add(new Version(1032L, "1.3.2"));
        versions.add(new Version(1031L, "1.3.1"));
        versions.add(new Version(1030L, "1.3"));
        versions.add(new Version(1025L, "1.2.5"));
        versions.add(new Version(1024L, "1.2.4"));
        versions.add(new Version(1023L, "1.2.3"));
        versions.add(new Version(1022L, "1.2.2"));
        versions.add(new Version(1021L, "1.2.1"));
        versions.add(new Version(1020L, "1.2"));
        versions.add(new Version(1010L, "1.1"));
        versions.add(new Version(1000L, "1.0"));

        versionRepository.saveAll(versions);
    }

    private void saveModes(){
        List<Mode> modes = new ArrayList<>();
        modes.add(new Mode(1L, "Sieć serwerów"));
        modes.add(new Mode(2L, "Survival"));
        modes.add(new Mode(3L, "Survival + Gildie"));
        modes.add(new Mode(4L, "Survival + Działki"));
        modes.add(new Mode(5L, "SkyBlock"));
        modes.add(new Mode(6L, "FreeBuild"));
        modes.add(new Mode(7L, "Vanilla"));
        modes.add(new Mode(8L, "CaveBlock"));
        modes.add(new Mode(9L, "Anarchia SMP"));
        modes.add(new Mode(10L, "Lifesteal SMP"));
        modes.add(new Mode(11L, "PvP"));
        modes.add(new Mode(12L, "ChestPvP"));
        modes.add(new Mode(13L, "SkyGen"));
        modes.add(new Mode(14L, "BedWars"));
        modes.add(new Mode(15L, "Creative"));
        modes.add(new Mode(16L, "Mega Enchant"));
        modes.add(new Mode(17L, "OneBlock"));
        modes.add(new Mode(18L, "RPG"));
        modes.add(new Mode(19L, "MegaDrop"));
        modes.add(new Mode(20L, "Parkour"));
        modes.add(new Mode(21L, "Earth SMP"));
        modes.add(new Mode(22L, "RealLife"));
        modes.add(new Mode(23L, "WaterBlock"));
        modes.add(new Mode(24L, "Roleplay"));
        modes.add(new Mode(25L, "MiniGames"));
        modes.add(new Mode(26L, "EasyHC"));
        modes.add(new Mode(27L, "CashBlock"));
        modes.add(new Mode(28L, "Anarchia"));
        modes.add(new Mode(29L, "EggWars"));
        modes.add(new Mode(30L, "Hardcore"));
        modes.add(new Mode(31L, "Jail"));
        modes.add(new Mode(32L, "MasterBuilders"));
        modes.add(new Mode(33L, "MediumHC"));
        modes.add(new Mode(34L, "Murder Mystery"));
        modes.add(new Mode(35L, "Duels"));
        modes.add(new Mode(36L, "KitPvP"));
        modes.add(new Mode(37L, "Spleef"));
        modes.add(new Mode(38L, "Survival Games"));
        modes.add(new Mode(39L, "The Walls"));
        modes.add(new Mode(40L, "TNTRun"));
        modes.add(new Mode(41L, "UHC"));
        modes.add(new Mode(42L, "The Bridge"));
        modes.add(new Mode(43L, "PaintBall"));
        modes.add(new Mode(43L, "SkyWars"));

        modeRepository.saveAll(modes);
    }

    private void saveRatingCategories(){
        List<RatingCategory> categories = new ArrayList<>();
        categories.add(new RatingCategory(1L, "Atmosfera gry", "Jak oceniasz ogólny klimat na serwerze?"));
        categories.add(new RatingCategory(2L, "Unikalne funkcje", "Jak oceniasz serwer pod względem unikalnych funkcji, trybów i dodatków?"));
        categories.add(new RatingCategory(3L, "Eventy i aktywność", "Jak oceniasz organizowane eventy oraz ich częstotliwość?"));
        categories.add(new RatingCategory(4L, "Ochrona przed oszustami", "Jak oceniasz zabezpieczenia przed cheaterami i oszustami?"));
        categories.add(new RatingCategory(5L, "Administracja", "Jak oceniasz pracę administracji, jej zaangażowanie w pomoc oraz łatwość kontaktu?"));

        ratingCategoryRepository.saveAll(categories);
    }
}