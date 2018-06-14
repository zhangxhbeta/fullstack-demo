package top.xhbeta.fullstack.scaffold.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import top.xhbeta.fullstack.scaffold.domain.PersistentAuditEvent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PersistenceAuditEventRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private PersistenceAuditEventRepository persistenceAuditEventRepository;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Test
  public void findByPrincipal() {

    String principal = "system";

    PersistentAuditEvent systemEvent = new PersistentAuditEvent();
    systemEvent.setAuditEventDate(Instant.now());
    systemEvent.setAuditEventType("auditType");
    systemEvent.setPrincipal(principal);

    Map<String, String> systemData = new HashMap<>();
    systemData.put("foo", "bar");
    systemEvent.setData(systemData);

    PersistentAuditEvent adminEvent = new PersistentAuditEvent();
    adminEvent.setAuditEventDate(Instant.now());
    adminEvent.setAuditEventType("auditType");
    adminEvent.setPrincipal("admin");

    Map<String, String> adminData = new HashMap<>();
    adminData.put("foo", "bar");
    adminEvent.setData(adminData);


    persistenceAuditEventRepository.save(systemEvent);
    persistenceAuditEventRepository.save(adminEvent);

    int row = JdbcTestUtils.countRowsInTable(jdbcTemplate, "xhb_persistent_audit_event");
    assertEquals(2, row);

    List<PersistentAuditEvent> events = persistenceAuditEventRepository.findByPrincipal(principal);
    assertEquals(1, events.size());

  }

  @Test
  public void findByAuditEventDateAfter() {
    String principal = "system";

    PersistentAuditEvent systemEvent = new PersistentAuditEvent();
    systemEvent.setAuditEventDate(Instant.now());
    systemEvent.setAuditEventType("auditType");
    systemEvent.setPrincipal(principal);

    Map<String, String> systemData = new HashMap<>();
    systemData.put("foo", "bar");
    systemEvent.setData(systemData);

    persistenceAuditEventRepository.save(systemEvent);

    List<PersistentAuditEvent> events = persistenceAuditEventRepository.findByAuditEventDateAfter(Instant.now().minus(1, ChronoUnit.DAYS));
    assertEquals(1, events.size());
  }

  @Test
  public void findByPrincipalAndAuditEventDateAfter() {
    String principal = "system";

    PersistentAuditEvent systemEvent = new PersistentAuditEvent();
    systemEvent.setAuditEventDate(Instant.now());
    systemEvent.setAuditEventType("auditType");
    systemEvent.setPrincipal(principal);

    Map<String, String> systemData = new HashMap<>();
    systemData.put("foo", "bar");
    systemEvent.setData(systemData);

    persistenceAuditEventRepository.save(systemEvent);

    List<PersistentAuditEvent> events = persistenceAuditEventRepository.findByPrincipalAndAuditEventDateAfter("system", Instant.now().minus(1, ChronoUnit.DAYS));
    assertEquals(1, events.size());


    events = persistenceAuditEventRepository.findByPrincipalAndAuditEventDateAfter("admin", Instant.now().minus(1, ChronoUnit.DAYS));
    assertEquals(0, events.size());
  }

  @Test
  public void findByPrincipalAndAuditEventDateAfterAndAuditEventType() {
    String principal = "system";
    String auditType = "auditType";

    PersistentAuditEvent systemEvent = new PersistentAuditEvent();
    systemEvent.setAuditEventDate(Instant.now());
    systemEvent.setAuditEventType(auditType);
    systemEvent.setPrincipal(principal);

    Map<String, String> systemData = new HashMap<>();
    systemData.put("foo", "bar");
    systemEvent.setData(systemData);

    persistenceAuditEventRepository.save(systemEvent);

    List<PersistentAuditEvent> events = persistenceAuditEventRepository.findByPrincipalAndAuditEventDateAfterAndAuditEventType("system",
      Instant.now().minus(1, ChronoUnit.DAYS), auditType);
    assertEquals(1, events.size());

    events = persistenceAuditEventRepository.findByPrincipalAndAuditEventDateAfterAndAuditEventType("admin",
      Instant.now().minus(1, ChronoUnit.DAYS), auditType);
    assertEquals(0, events.size());

    events = persistenceAuditEventRepository.findByPrincipalAndAuditEventDateAfterAndAuditEventType("admin",
      Instant.now().minus(1, ChronoUnit.DAYS), "log");
    assertEquals(0, events.size());
  }

  @Test
  public void findAllByAuditEventDateBetween() {
    String principal = "system";
    String auditType = "auditType";

    PersistentAuditEvent systemEvent = new PersistentAuditEvent();
    systemEvent.setAuditEventDate(Instant.now());
    systemEvent.setAuditEventType(auditType);
    systemEvent.setPrincipal(principal);

    Map<String, String> systemData = new HashMap<>();
    systemData.put("foo", "bar");
    systemEvent.setData(systemData);

    persistenceAuditEventRepository.save(systemEvent);

    Page<PersistentAuditEvent> events = persistenceAuditEventRepository.findAllByAuditEventDateBetween(
      Instant.now().minus(1, ChronoUnit.DAYS),
      Instant.now().plus(1, ChronoUnit.DAYS),
      PageRequest.of(0, 10)
    );

    assertEquals(1, events.getTotalPages());
    assertEquals(1, events.getTotalElements());

    PersistentAuditEvent event = events.getContent().get(0);

    assertEquals(systemEvent, event);
    assertNotNull(systemEvent.getId());
  }
}
