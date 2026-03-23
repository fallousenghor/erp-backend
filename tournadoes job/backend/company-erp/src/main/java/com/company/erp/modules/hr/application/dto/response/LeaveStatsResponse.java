package com.company.erp.modules.hr.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveStatsResponse {

  private long annual;
  private long sick;
  private long maternity;
  private long unpaid;
  private long exceptional;

}
