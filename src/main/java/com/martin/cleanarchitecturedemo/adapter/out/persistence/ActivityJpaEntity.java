package com.martin.cleanarchitecturedemo.adapter.out.persistence;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用戶活動 JPA 實體。
 */
@Entity
@Table(name = "activity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityJpaEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Column
  private LocalDateTime timestamp;

  /**
   * 此活動的帳戶所有者。
   */
  @Column
  private long ownerAccountId;
  /**
   * 被扣款的帳戶。
   */
  @Column
  private long sourceAccountId;
  /**
   * 被存款的帳戶。
   */
  @Column
  private long targetAccountId;
  /**
   * 被轉帳的金額。
   */
  @Column
  private long amount;
}
