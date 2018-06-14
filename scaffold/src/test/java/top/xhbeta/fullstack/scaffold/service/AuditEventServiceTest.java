package top.xhbeta.fullstack.scaffold.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import top.xhbeta.fullstack.scaffold.domain.PersistentAuditEvent;
import top.xhbeta.fullstack.scaffold.repository.PersistenceAuditEventRepository;
import top.xhbeta.fullstack.scaffold.support.audit.AuditEventConverter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class AuditEventServiceTest {

  @TestConfiguration
  static class AuditEventServiceImplTestContextConfiguration {

    @Bean
    public AuditEventService employeeService(PersistenceAuditEventRepository repository) {
      return new AuditEventService(repository, new AuditEventConverter());
    }
  }

  @Autowired
  private AuditEventService auditEventService;

  @MockBean
  private PersistenceAuditEventRepository auditEventRepository;

  private Pageable pageable;

  @Before
  public void setUp() {
    PersistentAuditEvent event = new PersistentAuditEvent();
    event.setPrincipal("system");
    event.setAuditEventType("auditType");
    event.setAuditEventDate(Instant.now());
    event.setId(0L);

    List<PersistentAuditEvent> events = new ArrayList<>();
    events.add(event);

    pageable = PageRequest.of(0, 10);

    Page<PersistentAuditEvent> pageEvents = new PageImpl<>(events, pageable, 1);

    Mockito.when(auditEventRepository.findAll(pageable))
      .thenReturn(pageEvents);


    Mockito.when(auditEventRepository.findById(0L))
      .thenReturn(Optional.of(event));
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
  }

  @Test
  public void find() {
    Optional<AuditEvent> result = auditEventService.find(0L);
    assertTrue(result.isPresent());

    assertEquals("auditType", result.get().getType());
  }
}
