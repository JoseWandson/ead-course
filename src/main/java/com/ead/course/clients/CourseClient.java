package com.ead.course.clients;

import com.ead.course.dtos.ResponsePageDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Component
public class CourseClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UtilsService utilsService;

    @Value("${ead.api.url.authuser}")
    private String requestUrlAuthuser;

    public Page<UserDto> getAllUsersByCourse(UUID courseId, Pageable pageable) {
        ResponsePageDto<UserDto> body = null;
        String url = requestUrlAuthuser + utilsService.createUrlGetAllUsersByCourse(courseId, pageable);

        log.info("Request URL: {}", url);

        try {
            var responseType = new ParameterizedTypeReference<ResponsePageDto<UserDto>>() {
            };
            ResponseEntity<ResponsePageDto<UserDto>> result = restTemplate
                    .exchange(url, HttpMethod.GET, null, responseType);
            body = result.getBody();
            List<UserDto> searchResult = Optional.ofNullable(body).map(ResponsePageDto::getContent)
                    .orElse(new ArrayList<>());
            log.debug("Response Number of Elements: {}", searchResult.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request /users", e);
        }

        log.info("Ending request /users courseId {}", courseId);

        return body;
    }
}
