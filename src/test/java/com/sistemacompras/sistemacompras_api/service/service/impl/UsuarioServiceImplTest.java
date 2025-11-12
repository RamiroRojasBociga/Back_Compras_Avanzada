package com.sistemacompras.sistemacompras_api.service.service.impl;

import com.sistemacompras.sistemacompras_api.dto.UsuarioRequestDto;
import com.sistemacompras.sistemacompras_api.dto.UsuarioResponseDto;
import com.sistemacompras.sistemacompras_api.dto.UsuarioUpdateDto; // NUEVO IMPORT
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

// Habilita Mockito en JUnit 5 para usar @Mock y @InjectMocks
@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    // Crea objetos simulados (mocks) para las dependencias del servicio
    @Mock
    private UsuarioRepository repository;

    @Mock
    private UsuarioMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    // Crea instancia real de UsuarioServiceImpl e inyecta los mocks
    @InjectMocks
    private UsuarioServiceImpl service;

    // Objetos de prueba
    private Usuario usuario;
    private UsuarioRequestDto requestDto;
    private UsuarioResponseDto responseDto;

    // Se ejecuta antes de cada test para preparar estado limpio
    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNombre("testuser");
        usuario.setEmail("test@email.com");
        usuario.setTelefono("3001234567");
        usuario.setPassword("contraseña-hasheada"); // Simula contraseña ya hasheada en BD

        requestDto = new UsuarioRequestDto();
        requestDto.setNombre("testuser");
        requestDto.setEmail("test@email.com");
        requestDto.setTelefono("3001234567");
        requestDto.setPassword("contraseña-plana"); // Contraseña que vendría del cliente

        responseDto = new UsuarioResponseDto();
        responseDto.setIdUsuario(1L);
        responseDto.setNombre("testuser");
        responseDto.setEmail("test@email.com");
        responseDto.setTelefono("3001234567");
    }

    // Test para create() con encriptación
    @Test
    void create_DebeEncriptarPasswordAntesDeGuardar() {
        // Given
        when(repository.existsByEmail("test@email.com")).thenReturn(false);
        when(mapper.toEntity(requestDto)).thenReturn(usuario);
        when(passwordEncoder.encode("contraseña-plana")).thenReturn("contraseña-hasheada");
        when(repository.save(any(Usuario.class))).thenReturn(usuario);
        when(mapper.toResponse(usuario)).thenReturn(responseDto);

        // When
        service.create(requestDto);

        // Then - Verificar que se llamó al método de encriptación
        verify(passwordEncoder).encode("contraseña-plana");

        // Capturar el objeto Usuario que se pasó a save
        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(usuarioCaptor.capture());

        // Verificar que la contraseña del objeto es la hasheada
        Usuario usuarioGuardado = usuarioCaptor.getValue();
        assertThat(usuarioGuardado.getPassword()).isEqualTo("contraseña-hasheada");
    }

    // CORREGIDO: Test para update() con UsuarioUpdateDto - encripta si se proporciona password
    @Test
    void update_DebeEncriptarPasswordSiSeProporcionaUnaNueva() {
        // Given
        Long usuarioId = 1L;
        UsuarioUpdateDto updateDto = new UsuarioUpdateDto(); // Usar UsuarioUpdateDto
        updateDto.setNombre("testuser");
        updateDto.setEmail("test@email.com");
        updateDto.setTelefono("3001234567");
        updateDto.setPassword("nueva-contraseña-plana");

        when(repository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(repository.existsByEmail("test@email.com")).thenReturn(false);
        when(passwordEncoder.encode("nueva-contraseña-plana")).thenReturn("nueva-contraseña-hasheada");
        when(repository.save(any(Usuario.class))).thenReturn(usuario);
        when(mapper.toResponse(usuario)).thenReturn(responseDto);

        // When
        service.update(usuarioId, updateDto);

        // Then - Verificar encriptación
        verify(passwordEncoder).encode("nueva-contraseña-plana");
        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(repository).save(usuarioCaptor.capture());

        Usuario usuarioGuardado = usuarioCaptor.getValue();
        assertThat(usuarioGuardado.getPassword()).isEqualTo("nueva-contraseña-hasheada");
    }

    // CORREGIDO: Test para update() sin password - NO debe encriptar
    @Test
    void update_NoDebeEncriptarPasswordSiNoSeProporciona() {
        // Given
        Long usuarioId = 1L;
        UsuarioUpdateDto updateDto = new UsuarioUpdateDto(); // Usar UsuarioUpdateDto
        updateDto.setNombre("nuevo-nombre");
        updateDto.setEmail("test@email.com");
        updateDto.setTelefono("3009876543");
        // No se establece password - debe mantenerse el actual

        when(repository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(repository.existsByEmail("test@email.com")).thenReturn(false);
        when(repository.save(any(Usuario.class))).thenReturn(usuario);
        when(mapper.toResponse(usuario)).thenReturn(responseDto);

        // When
        service.update(usuarioId, updateDto);

        // Then - Verificar que el método de encriptación NUNCA fue llamado
        verify(passwordEncoder, never()).encode(anyString());
    }

    // NUEVO: Test para update() con password vacío - NO debe encriptar
    @Test
    void update_NoDebeEncriptarPasswordSiEsVacio() {
        // Given
        Long usuarioId = 1L;
        UsuarioUpdateDto updateDto = new UsuarioUpdateDto();
        updateDto.setNombre("testuser");
        updateDto.setEmail("test@email.com");
        updateDto.setTelefono("3001234567");
        updateDto.setPassword("   "); // Password vacío o con espacios

        when(repository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(repository.existsByEmail("test@email.com")).thenReturn(false);
        when(repository.save(any(Usuario.class))).thenReturn(usuario);
        when(mapper.toResponse(usuario)).thenReturn(responseDto);

        // When
        service.update(usuarioId, updateDto);

        // Then - No debe encriptar password vacío
        verify(passwordEncoder, never()).encode(anyString());
    }

    // Otros tests CRUD y de búsqueda
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
                .hasMessageContaining("no encontrado");
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
