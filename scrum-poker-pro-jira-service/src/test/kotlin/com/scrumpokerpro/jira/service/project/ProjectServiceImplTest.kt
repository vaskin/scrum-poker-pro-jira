package com.scrumpokerpro.jira.service.project

import com.scrumpokerpro.jira.WithMockJwt
import com.scrumpokerpro.jira.entity.Token
import com.scrumpokerpro.jira.repository.TokenRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.support.TransactionTemplate
import java.time.OffsetDateTime
import java.util.UUID

@ActiveProfiles("test")
@SpringBootTest(
    properties = ["scrum-poker-pro-jira.atlassian.url=http://localhost:\${wiremock.server.port}"]
)
@WithMockJwt
@AutoConfigureWireMock(port = 0, stubs = ["classpath:/stubs"])
@Suppress("MaxLineLength")
class ProjectServiceImplTest {

    @Autowired
    lateinit var projectService: ProjectService

    @Autowired
    lateinit var tokenRepository: TokenRepository

    @Autowired
    lateinit var transactionTemplate: TransactionTemplate

    @Test
    fun `should get jira projects`() {
        transactional {
            tokenRepository.deleteAll()
        }
        transactional {
            Token(
                accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik16bERNemsxTVRoRlFVRTJRa0ZGT0VGRk9URkJOREJDTVRRek5EZzJSRVpDT1VKRFJrVXdNZyJ9.eyJodHRwczovL2F0bGFzc2lhbi5jb20vb2F1dGhDbGllbnRJZCI6IkJpZlJ1cWJEUHk4bHdRWENFUWVmQjd2aEJSMDRyNEN5IiwiaHR0cHM6Ly9hdGxhc3NpYW4uY29tL2VtYWlsRG9tYWluIjoiZ21haWwuY29tIiwiaHR0cHM6Ly9hdGxhc3NpYW4uY29tL3N5c3RlbUFjY291bnRJZCI6IjYwYmJkNmUyNDhiODk1MDA2OWMyNDFmNSIsImh0dHBzOi8vYXRsYXNzaWFuLmNvbS9zeXN0ZW1BY2NvdW50RW1haWxEb21haW4iOiJjb25uZWN0LmF0bGFzc2lhbi5jb20iLCJodHRwczovL2F0bGFzc2lhbi5jb20vdmVyaWZpZWQiOnRydWUsImh0dHBzOi8vYXRsYXNzaWFuLmNvbS9maXJzdFBhcnR5IjpmYWxzZSwiaHR0cHM6Ly9hdGxhc3NpYW4uY29tLzNsbyI6dHJ1ZSwiaXNzIjoiaHR0cHM6Ly9hdGxhc3NpYW4tYWNjb3VudC1wcm9kLnB1czIuYXV0aDAuY29tLyIsInN1YiI6ImF1dGgwfDU1NzA1ODo0YmUwZmM3Yi0wNzg3LTQ0YTQtYmZkZS00ZWVlNGNmYzQzMjEiLCJhdWQiOiJhcGkuYXRsYXNzaWFuLmNvbSIsImlhdCI6MTYyMzc5MDAxNiwiZXhwIjoxNjIzNzkzNjE2LCJhenAiOiJCaWZSdXFiRFB5OGx3UVhDRVFlZkI3dmhCUjA0cjRDeSIsInNjb3BlIjoicmVhZDpqaXJhLXdvcmsgcmVhZDpqaXJhLXVzZXIgcmVhZDptZSBvZmZsaW5lX2FjY2VzcyJ9.HzoAhE7gcZvdGAtwkWUj87FS6oTcfCJWagJL_MX2oAtsD6ZbtUzh7at3sH_SD_6nJM8L1QBPYxP2VzOBugIVPu9qKGgkqUS52rj-Rwp1M5TKkhBCdKSLOWbE5K8PU-93WtNYO9GWUZ4whMgTu_4a0Bid0H-ZWgg0M8ysRwKAdrUQIIEcpkBSROXbS2UsW_2n_JVOjnplVEvKEWaP_UCPPlm-eF-a-4UhILu2wpRWsW2n30V_eI9LiaxkC1z0NDa_tzxYiietclvHr8VYLHnCnSdap67IrazRBn0tKpBerMwvYHF8Lk2Jr2vgt9G7Ev_dDcoWshBW_fVx6DnN0BBv0A",
                refreshToken = "refreshToken",
                userId = UUID.fromString("a8c87ad7-e7e9-4d45-a7dc-e6c2a8dae4fa"),
                cloudId = "a5aebfc2-cddb-43e3-9675-6e090f4f5b1a",
                created = OffsetDateTime.now(),
                modified = OffsetDateTime.now()
            ).also {
                tokenRepository.save(it)
            }
        }
        val projects = projectService.getProjects().sortedBy { it.id }
        assertTrue(projects.size == 2)
        assertEquals("10000", projects[0].id)
        assertEquals("EX", projects[0].key)
        assertEquals("Example", projects[0].name)
        assertEquals("10001", projects[1].id)
        assertEquals("ABC", projects[1].key)
        assertEquals("Alphabetical", projects[1].name)
    }

    fun <R> transactional(block: () -> R): R {
        return transactionTemplate.execute { block.invoke() }!!
    }
}
