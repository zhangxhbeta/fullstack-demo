package top.xhbeta.fullstack.scaffold.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import top.xhbeta.fullstack.scaffold.domain.PersistentAuditEvent;
import top.xhbeta.fullstack.scaffold.repository.PersistenceAuditEventRepository;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class AuditEventServiceIntTest {

  @Autowired
  private AuditEventService auditEventService;

  @Autowired
  private PersistenceAuditEventRepository eventRepository;

  private Pageable pageable;

  @Before
  public void setUp() {
    PersistentAuditEvent event = new PersistentAuditEvent();
    event.setPrincipal("system");
    event.setAuditEventType("auditType");
    event.setAuditEventDate(Instant.now());

    pageable = PageRequest.of(0, 10);

    eventRepository.save(event);
  }

  @Test
  public void findAll() {
    Page<AuditEvent> result = auditEventService.findAll(pageable);
    assertEquals(1, result.getTotalElements());

    List<AuditEvent> eventList = result.getContent();
    assertEquals(1, eventList.size());

    AuditEvent event = eventList.get(0);
    assertEquals("auditType", event.getType());
  }

  @Test
  public void findByDates() {
    Page<AuditEvent> result = auditEventService.findByDates(Instant.now().minusSeconds(60), Instant.now().plusSeconds(60), pageable);
    assertEquals(1, result.getTotalElements());

    List<AuditEvent> eventList = result.getContent();
    assertEquals(1, eventList.size());

    AuditEvent event = eventList.get(0);
    assertEquals("auditType", event.getType());
  }

  @Test
  public void find() {

    List<PersistentAuditEvent> result = eventRepository.findAll();
    assertEquals(1, result.size());

    PersistentAuditEvent event = result.get(0);
    assertEquals("auditType", event.getAuditEventType());

    Optional<AuditEvent> auditEvent = auditEventService.find(event.getId());
    assertTrue(auditEvent.isPresent());

    assertEquals("auditType", auditEvent.get().getType());
  }
}
