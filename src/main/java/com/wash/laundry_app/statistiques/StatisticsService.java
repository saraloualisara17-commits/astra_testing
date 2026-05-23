package com.wash.laundry_app.statistiques;

import com.wash.laundry_app.command.Commande;
import com.wash.laundry_app.command.CommandeRepository;
import com.wash.laundry_app.command.CommandeStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class StatisticsService {

    private final CommandeRepository commandeRepository;

    public StatisticsDTO getTodayStatistics() {
        Long totalCommandesToday = commandeRepository.countTodayCommandes();
        Double revenuesToday = commandeRepository.getTodayRevenue();

        return StatisticsDTO.builder()
                .totalCommandesToday(totalCommandesToday)
                .revenuesToday(revenuesToday != null ? BigDecimal.valueOf(revenuesToday) : BigDecimal.ZERO)
                .commandesEnAttente(commandeRepository.countTodayCommandesByStatus(CommandeStatus.PENDING_PICKUP))
                .commandesValidees(commandeRepository.countTodayCommandesByStatus(CommandeStatus.PICKED_UP))
                .commandesEnTraitement(commandeRepository.countTodayCommandesByStatus(CommandeStatus.IN_PROCESS))
                .commandesPretes(commandeRepository.countTodayCommandesByStatus(CommandeStatus.READY_FOR_DELIVERY))
                .commandesLivrees(commandeRepository.countTodayCommandesByStatus(CommandeStatus.DELIVERED))
                .commandesPayees(commandeRepository.countTodayCommandesByStatus(CommandeStatus.DELIVERED))
                .dateDebut(LocalDate.now())
                .dateFin(LocalDate.now())
                .build();
    }

    public StatisticsDTO getOverallStatistics() {
        List<Commande> allCommandes = commandeRepository.findAll();

        long totalCommandes = allCommandes.size();
        BigDecimal totalRevenues = allCommandes.stream()
                .filter(c -> c.getStatus() == CommandeStatus.DELIVERED)
                .map(Commande::getMontantTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Long> commandesByStatus = new HashMap<>();
        for (CommandeStatus status : CommandeStatus.values()) {
            commandesByStatus.put(status.name(), commandeRepository.countByStatus(status));
        }

        return StatisticsDTO.builder()
                .totalCommandes(totalCommandes)
                .totalRevenues(totalRevenues)
                .commandesByStatus(commandesByStatus)
                .commandesEnAttente(commandeRepository.countByStatus(CommandeStatus.PENDING_PICKUP))
                .commandesValidees(commandeRepository.countByStatus(CommandeStatus.PICKED_UP))
                .commandesEnTraitement(commandeRepository.countByStatus(CommandeStatus.IN_PROCESS))
                .commandesPretes(commandeRepository.countByStatus(CommandeStatus.READY_FOR_DELIVERY))
                .commandesLivrees(commandeRepository.countByStatus(CommandeStatus.DELIVERED))
                .commandesPayees(commandeRepository.countByStatus(CommandeStatus.DELIVERED))
                .build();
    }

    public StatisticsDTO getStatisticsByDateRange(LocalDate dateDebut, LocalDate dateFin) {
        LocalDateTime start = dateDebut.atStartOfDay();
        LocalDateTime end = dateFin.atTime(LocalTime.MAX);

        List<Commande> commandes = commandeRepository.findByDateCreationBetween(start, end);

        long totalCommandes = commandes.size();
        BigDecimal totalRevenues = commandes.stream()
                .filter(c -> c.getStatus() == CommandeStatus.DELIVERED)
                .map(Commande::getMontantTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Long> commandesByStatus = new HashMap<>();
        for (CommandeStatus status : CommandeStatus.values()) {
            long count = commandes.stream().filter(c -> c.getStatus() == status).count();
            commandesByStatus.put(status.name(), count);
        }

        return StatisticsDTO.builder()
                .totalCommandes(totalCommandes)
                .totalRevenues(totalRevenues)
                .commandesByStatus(commandesByStatus)
                .commandesEnAttente(commandesByStatus.getOrDefault(CommandeStatus.PENDING_PICKUP.name(), 0L))
                .commandesValidees(commandesByStatus.getOrDefault(CommandeStatus.PICKED_UP.name(), 0L))
                .commandesEnTraitement(commandesByStatus.getOrDefault(CommandeStatus.IN_PROCESS.name(), 0L))
                .commandesPretes(commandesByStatus.getOrDefault(CommandeStatus.READY_FOR_DELIVERY.name(), 0L))
                .commandesLivrees(commandesByStatus.getOrDefault(CommandeStatus.DELIVERED.name(), 0L))
                .commandesPayees(commandesByStatus.getOrDefault(CommandeStatus.DELIVERED.name(), 0L))
                .dateDebut(dateDebut)
                .dateFin(dateFin)
                .build();
    }

    public DailyStatisticsDTO getDailyStatistics(LocalDate date) {
        Long nombreCommandes = commandeRepository.countCommandesByDate(date);
        Double revenusTotal = commandeRepository.getRevenueByDate(date);

        return DailyStatisticsDTO.builder()
                .date(date)
                .nombreCommandes(nombreCommandes)
                .revenusTotal(revenusTotal != null ? BigDecimal.valueOf(revenusTotal) : BigDecimal.ZERO)
                .nombreTapisTraites(0)
                .build();
    }

    public List<DailyStatisticsDTO> getLastNDaysStatistics(int days) {
        List<DailyStatisticsDTO> statistics = new java.util.ArrayList<>();
        for (int i = days - 1; i >= 0; i--) {
            statistics.add(getDailyStatistics(LocalDate.now().minusDays(i)));
        }
        return statistics;
    }

    public java.util.Map<String, Object> getStatusOverview() {
        java.util.Map<String, Object> overview = new HashMap<>();
        for (com.wash.laundry_app.command.CommandeStatus status : com.wash.laundry_app.command.CommandeStatus.values()) {
            overview.put(status.name(), commandeRepository.countByStatus(status));
        }
        return overview;
    }

    public StatisticsDTO getStatisticsByLivreur(Long livreurId, LocalDate dateDebut, LocalDate dateFin) {
        LocalDateTime start = dateDebut.atStartOfDay();
        LocalDateTime end = dateFin.atTime(LocalTime.MAX);

        List<Commande> commandes = commandeRepository.findByLivreurIdAndDateCreationBetween(livreurId, start, end);

        long totalCommandes = commandes.size();
        BigDecimal totalRevenues = commandes.stream()
                .filter(c -> c.getStatus() == CommandeStatus.DELIVERED)
                .map(Commande::getMontantTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return StatisticsDTO.builder()
                .totalCommandes(totalCommandes)
                .totalRevenues(totalRevenues)
                .dateDebut(dateDebut)
                .dateFin(dateFin)
                .build();
    }
}
