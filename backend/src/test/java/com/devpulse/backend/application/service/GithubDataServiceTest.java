
package com.devpulse.backend.application.service;

import com.devpulse.backend.domain.model.GithubData;
import com.devpulse.backend.domain.port.out.GithubDataRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubDataServiceTest {

    @Mock
    private GithubDataRepositoryPort repositoryPort;

    @InjectMocks
    private GithubDataService service;

    @Test
    void starsRatio_debeCalcularPromedioCorrectamente() {
        // GIVEN — preparar datos falsos
        GithubData data1 = new GithubData();
        data1.setStars(100);
        data1.setForks(5);
        data1.setRepos(10);
        data1.setCreatedAt(null);
        data1.setId(1L);
        data1.setTechnologyId(1L);
        GithubData data2 = new GithubData();
        data2.setStars(200);
        data2.setForks(10);
        data2.setRepos(5);
        data2.setCreatedAt(null);
        data2.setId(2L);
        data2.setTechnologyId(1L);

        // WHEN — llamar al servicio
        when(repositoryPort.findByTechnologyId(1L)).thenReturn(List.of(data1, data2));
        Double result = service.starsRationByTechnologyId(1L);
        // THEN — comprobar resultado
        assertEquals(150.0, result);
    }
}
            