package io.oneo.agon.modules.usuario.service;

import io.oneo.agon.modules.common.service.BaseService;
import io.oneo.agon.modules.usuario.exception.UsuarioServiceException;
import io.oneo.agon.modules.usuario.mapper.UsuarioMapper;
import io.oneo.agon.modules.usuario.model.Usuario;
import io.oneo.agon.modules.usuario.repository.UsuarioRepository;
import io.oneo.agon.modules.usuario.type.GrupoTipo;
import io.oneo.agon.modules.usuario.type.StatusUsuarioTipo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static java.lang.String.valueOf;

@ApplicationScoped
public class UsuarioService extends BaseService<Usuario, Long> implements IUsuarioService
{
    private final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Inject UsuarioRepository repo;

    @Inject UsuarioMapper mapper;

    public UsuarioMapper getMapper() { return this.mapper; }

    @Override
    public GrupoTipo validarGrupo(Integer grupoValor)
    {
        return switch (grupoValor) {
            case 1 -> GrupoTipo.ADMIN;
            case 2 -> GrupoTipo.CONVIDADO;
            case 3 -> GrupoTipo.EMPRESA;
            default -> GrupoTipo.PROFISSIONAL;
        };
    }

    @Override
    public StatusUsuarioTipo validarStatus(Integer statusValor)
    {
        return switch (statusValor) {
            case 1 -> StatusUsuarioTipo.ATIVO;
            case 2 -> StatusUsuarioTipo.INATIVO;
            default -> StatusUsuarioTipo.PENDENTE;
        };
    }

    @Override
    public Optional<Usuario> findByLogin(String login) throws UsuarioServiceException
    {
        try
        {
            var usuario = this.repo.findByLogin(login);
            if (!usuario.isPresent())
            {
                return Optional.empty();
            }
            return usuario;
        }
        catch (Exception e)
        {
            this.log.error(e.getMessage());
            throw new UsuarioServiceException("Erro ao buscar usuario pelo login", e);
        }
    }

    @Override
    public Optional<Usuario> findByEmail(String email) throws UsuarioServiceException
    {
        try
        {
            var usuario = this.repo.findByEmail(email);
            if (!usuario.isPresent())
            {
                return Optional.empty();
            }
            return usuario;
        }
        catch (Exception e)
        {
            this.log.error(e.getMessage());
            throw new UsuarioServiceException("Erro ao buscar usuario pelo email", e);
        }
    }

    public Optional<Usuario> findByModel(Usuario usuario) throws UsuarioServiceException
    {
        try
        {
            var user = this.findByID(usuario.getId());
            if (user.isPresent())
            {
                return user;
            }

            user = this.findByEmail(usuario.getEmail());
            if (user.isPresent())
            {
                return user;
            }

            return this.findByLogin(usuario.getLogin());
        }
        catch (Exception e)
        {
            this.log.error(e.getMessage());
            throw new UsuarioServiceException("Erro ao buscar usuario pelo login", e);
        }
    }

    public List<Usuario> listByGroup(Integer grupoID)
    {
        return this.repo.listByGrupo(this.validarGrupo(grupoID));
    }

    public List<Usuario> listByStatus(Integer statusID)
    {
        return this.repo.listByStatus(this.validarStatus(statusID));
    }

    @Transactional
    public Usuario addEdit(Usuario usuario) throws UsuarioServiceException
    {
        try
        {
            if (usuario.getId() == null)
            {
                usuario.setGrupo(this.validarGrupo(usuario.getGrupo().ordinal()));
                usuario.setStatus(this.validarStatus(usuario.getStatus().ordinal()));
                return this.create(usuario);
            }
            return this.update(usuario);
        }
        catch (Exception e)
        {
            this.log.error(e.getMessage());
            throw new UsuarioServiceException("Erro ao gravar os dados do usuário", e);
        }
    }



}