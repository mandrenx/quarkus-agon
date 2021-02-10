package io.oneo.agon.modules.telefone.service;

import io.oneo.agon.infra.service.BaseService;
import io.oneo.agon.modules.common.exception.BaseServiceException;
import io.oneo.agon.modules.telefone.mapper.TelefoneMapper;
import io.oneo.agon.modules.telefone.model.Telefone;
import io.oneo.agon.modules.telefone.repository.TelefoneRepository;
import io.oneo.agon.modules.telefone.resource.dto.TelefoneDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class TelefoneService extends BaseService<Telefone, Long> implements ITelefoneService
{
    private final Logger logger = LoggerFactory.getLogger(TelefoneService.class);

    @Inject TelefoneRepository repo;

    @Inject TelefoneMapper mapper;

    public TelefoneMapper getMapper() { return this.mapper; }

    private String validaFormatoNumero(String numero)
    {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(numero);
        String novo = matcher.group();
        return novo;
    }

    public Optional<Telefone> findByNumber(String numero)
    {
        if (!numero.equalsIgnoreCase(null))
        {
            String novo = this.validaFormatoNumero(numero);
            return this.repo.findByNumber(novo);
        }
        return Optional.empty();
    }

    @Transactional
    public void addEdit(Telefone telefone) throws BaseServiceException
    {
        try
        {
            if (telefone.getId() == null)
            {
                this.create(telefone);
            }
            this.update(telefone);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            throw new BaseServiceException("Erro ao gravar os dados do telefone!", e);
        }
    }
}