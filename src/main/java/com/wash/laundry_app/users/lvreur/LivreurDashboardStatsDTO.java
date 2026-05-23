package com.wash.laundry_app.users.lvreur;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LivreurDashboardStatsDTO {
    private long readyOrdersCount;
    private long pendingPickupCount;
    private long cancelledCount;
    private long missionsCount;
}
