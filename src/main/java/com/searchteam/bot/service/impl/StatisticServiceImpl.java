package com.searchteam.bot.service.impl;

import com.searchteam.bot.entity.Statistic;
import com.searchteam.bot.repository.StatisticRepository;
import com.searchteam.bot.service.StatisticService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository statisticRepository;

    @Override
    public void addTeamStatistic(String teamTitle) {
        Statistic statistic = new Statistic();
        statistic.setTeamTitle(teamTitle);
        if (!statisticRepository.existsByTeamTitle(teamTitle)) {
            statisticRepository.save(statistic);
        }
    }

    @Override
    public void incrementFoundUsers(String teamTitle) {
        Statistic statistic = statisticRepository.findByTeamTitle(teamTitle);
        statistic.setCountFoundUsers(statistic.getCountFoundUsers() + 1);
        statisticRepository.save(statistic);
    }

    @Override
    public List<Statistic> findAllStatisticsLimited() {
        return statisticRepository.findFirst5ByOrderByIdAsc();
    }

    @Override
    public List<Statistic> findAllStatistics() {
        return statisticRepository.findAll();
    }

    @Override
    @SneakyThrows
    public String createExcelStatistic() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Teams");

        List<Statistic> statistics = statisticRepository.findAll();
        int allCount = statistics.stream()
                .mapToInt(Statistic::getCountFoundUsers).sum();

        Row headerRow = sheet.createRow(0);
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("Общее количество найденных участников");
        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue(allCount);

        Row row = sheet.createRow(1);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue("Название команды");
        Cell cell2 = row.createCell(1);
        cell2.setCellValue("Количество найденных участников");

        int rowNum = 2;
        for (Statistic statistic : statistics) {
            row = sheet.createRow(rowNum++);
            cell1 = row.createCell(0);
            cell1.setCellValue(statistic.getTeamTitle());
            cell2 = row.createCell(1);
            cell2.setCellValue(statistic.getCountFoundUsers());
        }

        String filePath = "statistic.xlsx";
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

        workbook.close();
        return filePath;
    }

}
