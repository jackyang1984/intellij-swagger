package org.zalando.intellij.swagger.reference;

import com.intellij.json.psi.JsonLiteral;
import com.intellij.json.psi.JsonProperty;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zalando.intellij.swagger.traversal.JsonTraversal;

import java.util.List;
import java.util.stream.Collectors;

public class JsonResponseReference extends PsiReferenceBase<PsiElement> {

    private final String targetResponseKey;
    private final JsonTraversal jsonTraversal;

    public JsonResponseReference(@NotNull final JsonLiteral selectedElement,
                                 @NotNull final String targetResponseKey,
                                 @NotNull final JsonTraversal jsonTraversal) {
        super(selectedElement);
        this.targetResponseKey = targetResponseKey;
        this.jsonTraversal = jsonTraversal;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return getResponsesChildren().stream()
                .filter(jsonProperty -> targetResponseKey.equals(jsonProperty.getName()))
                .findFirst()
                .orElse(null);
    }

    private List<JsonProperty> getResponsesChildren() {
        return jsonTraversal.getChildrenOfDefinition("responses", getElement().getContainingFile()).stream()
                .filter(psiElement -> psiElement instanceof JsonProperty)
                .map(JsonProperty.class::cast)
                .collect(Collectors.toList());
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        return super.handleElementRename("#/responses/" + newElementName);
    }
}
