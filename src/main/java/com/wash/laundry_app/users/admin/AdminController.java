package com.wash.laundry_app.users.admin;

import com.wash.laundry_app.clients.ClientDto;
import com.wash.laundry_app.clients.ClientRegisterRequest;
import com.wash.laundry_app.clients.services.ClientService;
import com.wash.laundry_app.command.*;
import com.wash.laundry_app.command.services.CommandeQueryService;
import com.wash.laundry_app.statistiques.*;
import com.wash.laundry_app.users.*;
import com.wash.laundry_app.users.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class AdminController {

    private final UserService userService;
    private final ClientService clientService;
    private final CommandeService commandeService;
    private final CommandeQueryService queryService;
    private final StatisticsService statisticsService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserRegisterRequest request, UriComponentsBuilder uriBuilder) {
        return userService.createUser(request, uriBuilder);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getSingleUser(id);
    }

    @GetMapping("/active")
    public List<UserDto> activeUsers() {
        return userService.getAllActiveUsers();
    }

    @GetMapping("/inactive")
    public List<UserDto> inActiveUsers() {
        return userService.getAllInActiveUsers();
    }

    @PatchMapping("/{id}/deactivate")
    public void inActiveUser(@PathVariable Long id) {
        userService.deactivateUser(id);
    }

    @PatchMapping("/{id}/activate")
    public void activeUser(@PathVariable Long id) {
        userService.activateUser(id);
    }

    @DeleteMapping("/{id}")
    public void deleteInActiveUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/map")
    public ResponseEntity<?> getOrdersForMap(@RequestParam(required = false) Long livreurId) {
        return ResponseEntity.ok(queryService.getOrdersForMap(livreurId));
    }

    @GetMapping("/commandes")
    public ResponseEntity<AdminOrdersResponseDTO> allCommandes(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) Boolean paidDebts,
            @RequestParam(required = false) Boolean activeOnly,
            @RequestParam(required = false) Boolean selfSubmitted,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long livreurId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String sort) {
        return ResponseEntity.ok(queryService.getFilteredCommands(
                status, mode, paidDebts, activeOnly, selfSubmitted, dateDebut, dateFin, search, livreurId, page, size, sort));
    }

    @GetMapping("/commandes/export-csv")
    public ResponseEntity<byte[]> exportCommandesCsv() {
        byte[] csvData = queryService.exportCommandesToCsv();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=commandes.csv");
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
        return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
    }

    @GetMapping("/commandes/{id}")
    public ResponseEntity<CommandeDTO> getCommande(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.getCommandeDtoById(id));
    }

    @PutMapping("/commandes/{id}")
    public ResponseEntity<CommandeDTO> updateCommande(@PathVariable Long id, @Valid @RequestBody UpdateCommandeRequest request) {
        return ResponseEntity.ok(commandeService.updateCommande(id, request));
    }

    @PatchMapping("/commandes/{id}/status")
    public ResponseEntity<CommandeDTO> updateCommandeStatus(@PathVariable Long id, @Valid @RequestBody UpdateCommandeStatusRequest request) {
        return ResponseEntity.ok(commandeService.updateStatus(id, request));
    }

    @PatchMapping("/commandes/{id}/delivery-driver")
    public ResponseEntity<Void> assignDeliveryDriver(@PathVariable Long id, @RequestBody UpdateDeliveryDriverRequest request) {
        UpdateCommandeRequest update = new UpdateCommandeRequest();
        update.setDeliveryDriverId(request.getDeliveryDriverId());
        update.setScheduledDeliveryDate(request.getScheduledDeliveryDate());
        commandeService.updateCommande(id, update);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/commandes/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable Long id) {
        commandeService.deleteCommande(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clients")
    public ResponseEntity<List<ClientDto>> getClients(@RequestParam(required = false) String search) {
        return ResponseEntity.ok(clientService.getClientsFiltered(search));
    }

    @GetMapping("/clients/statistics")
    public ResponseEntity<ClientStatisticsDto> getClientStatistics() {
        return ResponseEntity.ok(clientService.getClientStatistics());
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<ClientDto> getClient(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @GetMapping("/clients/{id}/commandes")
    public ResponseEntity<List<CommandeDTO>> getClients(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientCommandes(id));
    }

    @GetMapping("/statistics/today")
    public ResponseEntity<StatisticsDTO> getTodayStatistics() {
        return ResponseEntity.ok(statisticsService.getTodayStatistics());
    }

    @GetMapping("/statistics/overall")
    public ResponseEntity<StatisticsDTO> getOverallStatistics() {
        return ResponseEntity.ok(statisticsService.getOverallStatistics());
    }

    @PostMapping("/statistics/date-range")
    public ResponseEntity<StatisticsDTO> getStatisticsByDateRange(@Valid @RequestBody DateRangeRequest request) {
        return ResponseEntity.ok(statisticsService.getStatisticsByDateRange(request.getDateDebut(), request.getDateFin()));
    }

    @GetMapping("/statistics/date-range")
    public ResponseEntity<StatisticsDTO> getStatisticsByDateRangeGet(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(statisticsService.getStatisticsByDateRange(dateDebut, dateFin));
    }

    @GetMapping("/statistics/daily")
    public ResponseEntity<DailyStatisticsDTO> getDailyStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(statisticsService.getDailyStatistics(date));
    }

    @GetMapping("/statistics/last-days")
    public ResponseEntity<List<DailyStatisticsDTO>> getLastNDaysStatistics(@RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(statisticsService.getLastNDaysStatistics(days));
    }

    @GetMapping("/statistics/livreur/{livreurId}")
    public ResponseEntity<StatisticsDTO> getStatisticsByLivreur(
            @PathVariable Long livreurId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return ResponseEntity.ok(statisticsService.getStatisticsByLivreur(livreurId, dateDebut, dateFin));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<?> changeUserPassword(@PathVariable Long id, @Valid @RequestBody UsersChangePassword password) {
        userService.changePassword(id, password.getPassword());
        return ResponseEntity.ok(Map.of("message", "Mot de passe mis à jour avec succès"));
    }

    @PostMapping("/commandes")
    public ResponseEntity<CommandeDTO> createCommande(@Valid @RequestBody CreateCommandeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commandeService.createCommande(request));
    }

    @PostMapping("/clients")
    public ResponseEntity<ClientDto> createClient(@Valid @RequestBody ClientRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(request));
    }

    @PutMapping("/clients/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @Valid @RequestBody ClientRegisterRequest request) {
        return ResponseEntity.ok(clientService.updateClient(id, request));
    }
}
