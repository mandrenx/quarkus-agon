package io.oneo.agon.modules.cidade.service;

import io.oneo.agon.modules.cidade.exception.CidadeServiceException;
import io.oneo.agon.modules.common.service.BaseService;
import io.oneo.agon.modules.cidade.mapper.CidadeMapper;
import io.oneo.agon.modules.cidade.model.Cidade;
import io.oneo.agon.modules.cidade.repository.CidadeRepository;
import io.oneo.agon.modules.estado.model.Estado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CidadeService extends BaseService<Cidade, Long>
{
    private final Logger logger = LoggerFactory.getLogger(CidadeService.class);

    @Inject CidadeRepository repo;

    @Inject CidadeMapper mapper;

    public CidadeMapper getMapper() { return this.mapper; }

    public Optional<Cidade> findByName(String name)
    {
        if (name.equalsIgnoreCase(null))
        {
            return Optional.empty();
        }
        return this.repo.findByName(name);
    }

    public Optional<Cidade> findByCode(String code)
    {
        if (code.equals(null))
        {
            return Optional.empty();
        }
        return this.repo.findByCode(code);
    }

    public List<Cidade> listByState(Estado estado)
    {
        return this.repo.listByState(estado);
    }

    public Optional<Cidade> findByModel(Cidade cidade) throws CidadeServiceException
    {
        var city = this.findByID(cidade.getId());
        if (city.isPresent())
        {
            return city;
        }

        city = this.findByCode(cidade.getCodigo());
        if (city.isPresent())
        {
            return city;
        }
        return Optional.empty();
    }

    @Transactional
    public Cidade addEdit(Cidade cidade) throws CidadeServiceException
    {
        try
        {
            if (cidade.getId() == null)
            {
                return this.create(cidade);
            }
            return this.update(cidade);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            throw new CidadeServiceException("Erro ao gravar os dados do usuário", e);
        }
    }


}