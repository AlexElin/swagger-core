package io.swagger.v3.jaxrs2.ext;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.v3.core.util.Configuration;
import io.swagger.v3.jaxrs2.ResolvedParameter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class AbstractOpenAPIExtension implements OpenAPIExtension {

    protected boolean openapi31;
    protected Schema.SchemaResolution schemaResolution;
    protected Configuration configuration = new Configuration();

    @Override
    public String extractOperationMethod(Method method, Iterator<OpenAPIExtension> chain) {
        if (chain.hasNext()) {
            return chain.next().extractOperationMethod(method, chain);
        } else {
            return null;
        }
    }

    @Override
    public ResolvedParameter extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip,
                                               Components components, javax.ws.rs.Consumes classConsumes,
                                               javax.ws.rs.Consumes methodConsumes, boolean includeRequestBody, JsonView jsonViewAnnotation, Iterator<OpenAPIExtension> chain) {
        if (chain.hasNext()) {
            return chain.next().extractParameters(annotations, type, typesToSkip, components, classConsumes, methodConsumes, includeRequestBody, jsonViewAnnotation, chain);
        } else {
            return new ResolvedParameter();
        }
    }

    @Override
    public void decorateOperation(Operation operation, Method method, Iterator<OpenAPIExtension> chain) {
        if (chain.hasNext()) {
            chain.next().decorateOperation(operation, method, chain);
        }
    }

    protected boolean shouldIgnoreClass(Class<?> cls) {
        return false;
    }

    protected boolean shouldIgnoreType(Type type, Set<Type> typesToSkip) {
        if (typesToSkip.contains(type)) {
            return true;
        }
        if (shouldIgnoreClass(constructType(type).getRawClass())) {
            typesToSkip.add(type);
            return true;
        }
        return false;
    }

    protected JavaType constructType(Type type) {
        return TypeFactory.defaultInstance().constructType(type);
    }

    @Override
    public void setOpenAPI31(boolean openapi31) {
        this.openapi31 = openapi31;
    }

    @Override
    public void setSchemaResolution(Schema.SchemaResolution schemaResolution) {
        this.schemaResolution = schemaResolution;
    }
    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        if (configuration != null) {
            this.openapi31 = configuration.isOpenAPI31() != null ? configuration.isOpenAPI31() : false;
            this.schemaResolution = configuration.getSchemaResolution();
        }
    }

}
