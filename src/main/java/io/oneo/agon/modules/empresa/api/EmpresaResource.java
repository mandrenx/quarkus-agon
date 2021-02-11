package io.oneo.agon.modules.empresa.api;

import io.oneo.agon.modules.common.exception.BaseServiceException;
import io.oneo.agon.modules.empresa.model.Empresa;
import io.oneo.agon.modules.empresa.resource.dto.EmpresaDTO;
import io.oneo.agon.modules.empresa.service.EmpresaService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("empresa")
@Produces("application/json")
@Consumes("application/json")
public class EmpresaResource
{
    @Inject EmpresaService service;

    @GET
    @Operation(description = "Lista Empresa")
    @Tag(name="empresa")
    @APIResponse(responseCode = "200", description = "Ok")
    public Response list()
    {
        var list = this.service.getMapper().convertToDtoList(this.service.list());
        return Response
                .ok(list)
                .build();
    }

    @GET
    @Path("/{id}")
    @Operation(description = "Buscar usuário pelo ID")
    @Tag(name="empresa")
    @APIResponse(responseCode = "200", description = "Ok")
    public Response findByID(@PathParam("id") Long id)
    {
        var empresa = this.service.findByID(id);
        var dto = this.service.getMapper().convertToDTO(empresa.get());
        return Response
                .ok(dto)
                .build();
    }

    @GET
    @Path("/cnpj/{cnpj}")
    @Operation(description = "Busca empresa pelo CNPJ")
    @Tag(name="empresa")
    @APIResponse(responseCode = "200", description = "Ok")
    public Response findByCNPJ(@PathParam("cnpj") String cnpj)
    {
        var empresa = this.service.findByCNPJ(cnpj);
        if (!empresa.isPresent())
        {
            return Response
                    .noContent()
                    .build();
        }
        var dto = this.service.getMapper().convertToDTO(empresa.get());
        return Response
                .ok(dto)
                .build();
    }

    @POST
    @Operation(description = "Cadastra um novo usuário")
    @Tag(name="empresa")
    @APIResponse(responseCode = "200", description = "Ok")
    public Response create(@RequestBody EmpresaDTO dto) throws BaseServiceException
    {
        var empresa = this.service.getMapper().convertToModel(dto);
        this.service.addEdit(empresa);
        dto = this.service.getMapper().convertToDTO(empresa);
        return Response
                .ok(dto)
                .build();
    }

    @PUT
    @Operation(description = "Atualiza os dados do usuário")
    @Tag(name="empresa")
    @APIResponse(responseCode = "200", description = "Ok")
    public Response update(@RequestBody EmpresaDTO dto) throws BaseServiceException
    {
        var empresa = this.service.getMapper().convertToModel(dto);
        this.service.addEdit(empresa);
        dto = this.service.getMapper().convertToDTO(empresa);
        return Response
                .ok(dto)
                .build();
    }
}