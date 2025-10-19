package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.UsuarioRequestDto;
import com.sistemacompras.sistemacompras_api.dto.UsuarioResponseDto;
import com.sistemacompras.sistemacompras_api.entity.Usuario;
import com.sistemacompras.sistemacompras_api.exception.ResourceNotFoundException;
import com.sistemacompras.sistemacompras_api.mapper.UsuarioMapper;
import com.sistemacompras.sistemacompras_api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class): Habilita Mockito en JUnit 5 para poder usar @Mock y @InjectMocks.
@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    // @Mock: Crea un objeto simulado (mock) para las dependencias del servicio.
    @Mock
    private UsuarioRepository repository;

    @Mock
    private UsuarioMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    // @InjectMocks: Crea una instancia real de UsuarioServiceImpl e inyecta los mocks.
    @InjectMocks
    private UsuarioServiceImpl service;

    // --- Objetos de prueba ---
    private Usuario usuario;
    private UsuarioRequestDto requestDto;
    private UsuarioResponseDto responseDto;

    // @BeforeEach: Se ejecuta antes de cada test para preparar un estado limpio.
    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombre("testuser");
        usuario.setEmail("test@email.com");
        usuario.setPassword("contraseña-hasheada"); // Simula una contraseña ya hasheada en la BD.

        requestDto = new UsuarioRequestDto();
        requestDto.setNombre("testuser");
        requestDto.setEmail("test@email.com");
        requestDto.setPassword("contraseña-plana"); // La contraseña que vendría del cliente.

        responseDto = new UsuarioResponseDto();
        responseDto.setIdUsuario(1L);
        responseDto.setNombre("testuser");
        responseDto.setEmail("test@email.com");
    }

    // --- Test para create() con encriptación ---
    @Test
    void create_DebeEncriptarPasswordAntesDeGuardar() {
        // Given
        when(repository.existsByNombreIgnoreCase("testuser")).thenReturn(false);
        when(mapper.toEntity(requestDto)).thenReturn(usuario);
        when(passwordEncoder.encode("contraseña-plana")).thenReturn("contraseña-hasheada");
        when(repository.save(any(Usuario.class))).thenReturn(usuario);
        when(mapper.toResponse(usuario)).thenReturn(responseDto);

        // When
        service.create(requestDto);

        // Then
        // VERIFICACIÓN CLAVE 1: Aseguramos que se llamó al método de encriptación.
        verify(passwordEncoder).encode("contraseña-plana");

        // VERIFICACIÓN CLAVE 2: Capturamos el objeto `Usuario` que se pasó al método `save`...
        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(usuarioCaptor.capture());

        // ...y verificamos que la contraseña de ese objeto es la que fue "hasheada" por nuestro mock.
        Usuario usuarioGuardado = usuarioCaptor.getValue();
        assertThat(usuarioGuardado.getPassword()).isEqualTo("contraseña-hasheada");
    }

    // --- Tests para update() con encriptación ---
    @Test
    void update_DebeEncriptarPasswordSiSeProporcionaUnaNueva() {
        // Given
        Long usuarioId = 1L;
        UsuarioRequestDto updateDto = new UsuarioRequestDto();
        updateDto.setPassword("nueva-contraseña-plana");

        when(repository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("nueva-contraseña-plana")).thenReturn("nueva-contraseña-hasheada");
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        service.update(usuarioId, updateDto);

        // Then
        verify(passwordEncoder).encode("nueva-contraseña-plana");
        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(usuarioCaptor.capture());

        Usuario usuarioGuardado = usuarioCaptor.getValue();
        assertThat(usuarioGuardado.getPassword()).isEqualTo("nueva-contraseña-hasheada");
    }

    @Test
    void update_NoDebeEncriptarPasswordSiNoSeProporciona() {
        // Given
        Long usuarioId = 1L;
        UsuarioRequestDto updateDto = new UsuarioRequestDto();
        updateDto.setNombre("nuevo-nombre");

        when(repository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        service.update(usuarioId, updateDto);

        // Then
        // VERIFICACIÓN CLAVE: Aseguramos que el método de encriptación NUNCA fue llamado.
        verify(passwordEncoder, never()).encode(anyString());
    }

    // --- Otros tests CRUD y de búsqueda ---
    @Test
    void findAll_DebeRetornarListaDeUsuarios() {
        when(repository.findAll()).thenReturn(List.of(usuario));
        when(mapper.toResponseList(anyList())).thenReturn(List.of(responseDto));

        List<UsuarioResponseDto> result = service.findAll();

        assertThat(result).isNotNull().hasSize(1);
        verify(repository).findAll();
    }

    @Test
    void findById_DebeRetornarUsuarioCuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        when(mapper.toResponse(usuario)).thenReturn(responseDto);

        UsuarioResponseDto result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getIdUsuario()).isEqualTo(1L);
    }

    @Test
    void findById_DebeLanzarExcepcionCuandoNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("no encontrada");
    }

    @Test
    void findByEmail_DebeRetornarUsuarioCuandoExiste() {
        when(repository.findByEmail("test@email.com")).thenReturn(Optional.of(usuario));
        when(mapper.toResponse(usuario)).thenReturn(responseDto);

        UsuarioResponseDto result = service.findByEmail("test@email.com");

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@email.com");
    }

    @Test
    void delete_DebeEliminarUsuario() {
        Long usuarioId = 1L;
        when(repository.existsById(usuarioId)).thenReturn(true);

        service.delete(usuarioId);

        verify(repository).deleteById(usuarioId);
    }
}
