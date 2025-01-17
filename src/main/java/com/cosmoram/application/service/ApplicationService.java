package com.cosmoram.application.service;

import com.cosmoram.application.entity.Application;
import com.cosmoram.application.exception.ApplicationBadRequestException;
import com.cosmoram.application.exception.ApplicationError;
import com.cosmoram.application.repository.ApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public final class ApplicationService {

    private final Logger log = LoggerFactory.getLogger(
            ApplicationService.class);
    public static final int SESSION_ID_LENGTH_LOWER_LIMIT = 3;
    public static final int SESSION_ID_LENGTH_UPPER_LIMIT = 128;
    public static final int USER_ID_LENGTH_LOWER_LIMIT = 3;
    public static final int USER_ID_LENGTH_UPPER_LIMIT = 10;
    public static final int CORRELATION_ID_LENGTH_LOWER_LIMIT = 3;
    public static final int CORRELATION_ID_LENGTH_UPPER_LIMIT = 360;

    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Application save(Application application) throws
            ApplicationBadRequestException {
        log.info("Service Save is being executed");
        Optional<Application> existingApplication =
                applicationRepository.findByCode(application.getCode());
        if (existingApplication.isPresent())
            throw new ApplicationBadRequestException(List.of(
                    new ApplicationError("code",
                            "cosomoram.application.duplicate_request",
                            "Duplicate Request")));

        return applicationRepository.save(application);
    }

    public void validateHeaders(String sessionId, String userId,
                                String correlationId)
            throws ApplicationBadRequestException {
        List<ApplicationError> errorList = new ArrayList<>();

        validateHeader(errorList, sessionId, "sessionId",
                new int[]{SESSION_ID_LENGTH_LOWER_LIMIT,
                        SESSION_ID_LENGTH_UPPER_LIMIT});
        validateHeader(errorList, userId, "userId",
                new int[]{USER_ID_LENGTH_LOWER_LIMIT,
                        USER_ID_LENGTH_UPPER_LIMIT});
        validateHeader(errorList, correlationId, "correlationId",
                new int[]{CORRELATION_ID_LENGTH_LOWER_LIMIT,
                        CORRELATION_ID_LENGTH_UPPER_LIMIT});

        if (!errorList.isEmpty()) {
            throw new ApplicationBadRequestException(errorList);
        }
    }

    //TODO Give a proper error message
    private void validateHeader(List<ApplicationError> errorList, String header,
                                String headerName, int[] size) {

        int headerSize = header.length();

        if (headerSize < size[0] || headerSize > size[1]) {
            errorList.add(new ApplicationError(headerName, "",
                    "Size failure"));
        }

        if (!header.matches("^[A-Za-z0-9]*$")) {
            errorList.add(new ApplicationError(headerName, "",
                    "Regex validation failure"));
        }
    }
}
