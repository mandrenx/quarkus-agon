package io.oneo.agon.modules.usuario.api;

import io.oneo.agon.modules.usuario.exception.UsuarioException;
import io.oneo.agon.modules.usuario.resource.UsuarioDTO;
import io.oneo.agon.modules.usuario.service.UsuarioService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/api/usuarios")
@Produces("application/json")
@Consumes("application/json")
public class UsuarioResource
{
    @Inject UsuarioService service;

    @GET
    @Operation(description = "Lista todos os usuários")
    @Tag(name = "usuarios")
    @APIResponse(responseCode = "200", description = "Ok")
    public Response list()
    {
        return Response
                .ok(this.service.getMapper().dtoList(this.service.list()))
                .build();
    }

    @GET
    @Path("/{id}")
    @Operation(description = "Buscar usuário pelo ID")
    @Tag(name = "usuarios")
    @APIResponse(responseCode = "200", description = "Ok")
    public Response findByID(@PathParam("id") Long id)
    {
        var usuario = this.service.findByID(id);
        return Response
                .ok(this.service.getMapper().getDTO(usuario.get()))
                .build();
    }

    @GET
    @Path("/login/{login}")
    @Operation(description = "Buscar usuário pelo LOGIN")
    @Tag(name = "usuarios")
    @APIResponse(responseCode = "200", description = "Ok")
    public Response findByLogin(@PathParam("login") String login)
    {
        var usuario = this.service.findByLogin(login);
        return Response
                .ok(this.service.getMapper().getDTO(usuario.get()))
                .build();
    }

    @POST
    @Operation(description = "Cadastra um novo usuário")
    @Tag(name = "usuarios")
    @APIResponse(responseCode = "200", description = "Ok")
    public Response create(@RequestBody UsuarioDTO dto) throws UsuarioException
    {
        var usuario = this.service.getMapper().getModel(dto);
        this.service.addEdit(usuario);
        return Response
                .ok(this.service.getMapper().getDTO(usuario))
                .build();
    }

    @POST
    @Path("/validate")
    @Operation(description = "Valida um usuário")
    @Tag(name = "usuarios")
    @APIResponse(responseCode = "200", description = "Ok")
    public Response validate(@RequestBody UsuarioDTO dto) throws UsuarioException
    {
        var user = this.service.validate(this.service.getMapper().getModel(dto));
        if (user.isPresent())
        {
            return Response
                    .ok(this.service.getMapper().getDTO(user.get()))
                    .build();
        }
        return this.create(dto);
    }

    @PUT
    @Operation(description = "Atualiza os dados do usuário")
    @Tag(name = "usuarios")
    @APIResponse(responseCode = "200", description = "Ok")
    public Response update(@RequestBody UsuarioDTO dto) throws UsuarioException
    {
        var usuario = this.service.getMapper().getModel(dto);
        this.service.addEdit(usuario);
        return Response
                .ok(this.service.getMapper().getDTO(usuario))
                .build();
    }

    @DELETE
    @Operation(description = "Atualiza os dados do usuário")
    @Tag(name = "usuarios")
    @APIResponse(responseCode = "200", description = "Ok")
    public Response delete(@RequestBody UsuarioDTO dto) throws UsuarioException
    {
        this.service.delete(this.service.getMapper().getModel(dto));
        return Response
                .noContent()
                .build();
    }


}