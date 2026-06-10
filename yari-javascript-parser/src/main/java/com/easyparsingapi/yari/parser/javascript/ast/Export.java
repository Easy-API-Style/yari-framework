/*
 * Copyright (c) 2025 Easy API
 * Website : https://easyparsingapi.com/
 * GitHub  : https://github.com/Easy-API-Style/yari-framework
 * Contact : easy.api.contact@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easyparsingapi.yari.parser.javascript.ast;

import java.util.List;
import java.util.Objects;

import com.easyparsingapi.yari.core.ast.AstNode;
import com.easyparsingapi.yari.core.util.CollectionUtil;
import com.easyparsingapi.yari.parsec.location.SourceLocation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * AST node representing a JavaScript {@code export} declaration.
 * Captures whether the export is a default export and the exported definition node.
 */
@JsonPropertyOrder({"defaultExport", "definition", "sourceLocation"})
public class Export implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The defaultExport. */
    @JsonProperty("defaultExport") 
    private final boolean defaultExport;
    /** The definition. */
    @JsonProperty("definition") 
    private final JavascriptNode definition;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation") 
    private SourceLocation sourceLocation;

    /**
     * Constructs an {@code Export} node without source location information.
     *
     * @param defaultExport {@code true} if this is a {@code export default} declaration
     * @param definition    the exported definition node
     */
    public Export(final boolean defaultExport,
                  final JavascriptNode definition) {
        this(defaultExport, definition, null);
    }

    /**
     * Constructs an {@code Export} node with full source location information.
     *
     * @param defaultExport  {@code true} if this is a {@code export default} declaration
     * @param definition     the exported definition node
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public Export(@JsonProperty("defaultExport") final boolean defaultExport,
                  @JsonProperty("definition") final JavascriptNode definition,
                  @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.defaultExport = defaultExport;
        this.definition = definition;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(defaultExport, definition);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent node of this export node in the AST.
     *
     * @param parent the parent node to associate
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns {@code true} if this is a {@code export default} declaration.
     *
     * @return {@code true} for default exports
     */
    public boolean isDefault() {
        return defaultExport;
    }

    /**
     * Returns the node representing the exported definition.
     *
     * @return the exported definition node
     */
    public JavascriptNode getDefinition() {
        return definition;
    }
    
    /** {@inheritDoc} */
    @Override
    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }

    /** {@inheritDoc} */
    @Override
    public void setSourceLocation(final SourceLocation sourceLocation) {
        this.sourceLocation = sourceLocation;
    }
    
    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(defaultExport, definition, sourceLocation);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof AstNode node) {
            return equalsNode(node) 
                      && Objects.equals(sourceLocation, node.getSourceLocation());
        }
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean equalsNode(final AstNode obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Export other = (Export) obj;
        return defaultExport == other.defaultExport 
                && Objects.equals(definition, other.definition);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Export.class.getSimpleName());
        result.append(" [default=");
        result.append(defaultExport);
        result.append(", definition=");
        result.append(definition);
        if (sourceLocation != null) {
            result.append(", sourceLocation=");
            result.append(sourceLocation);
        }
        result.append("]");
        return result.toString();
    }

    /*
     * 
     * CLASS 
     * 
     */
    /**
     * Represents a named export block ({@code export { foo, bar as baz }}), containing
     * an ordered list of {@link ExportReference} entries.
     */
    @JsonPropertyOrder({"exportReferences", "sourceLocation"})
    public static class ExportBlock implements JavascriptNode {

        private static final long serialVersionUID = 1L; 
        
        /** The exportReferences. */
        @JsonProperty("exportReferences") 
        private final List<ExportReference> exportReferences;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation") 
        private SourceLocation sourceLocation;

        /**
         * Constructs an {@code ExportBlock} without source location information.
         *
         * @param exportReferences the list of named export references
         */
        public ExportBlock(final List<ExportReference> exportReferences) {
            this(exportReferences, null);
        }

        /**
         * Constructs an {@code ExportBlock} with full source location information.
         *
         * @param exportReferences the list of named export references
         * @param sourceLocation   the source location of this node, or {@code null}
         */
        @JsonCreator
        public ExportBlock(@JsonProperty("exportReferences") final List<ExportReference> exportReferences,
                           @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.exportReferences = CollectionUtil.nullToEmpty(exportReferences);
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes(exportReferences);
        }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }
        
        /**
         * Sets the parent node of this export block in the AST.
         *
         * @param parent the parent node to associate
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the number of export references contained in this block.
         *
         * @return the number of export references
         */
        public int size() {
            return exportReferences.size();
        }

        /**
         * Returns the list of export references contained in this block.
         *
         * @return an unmodifiable list of {@link ExportReference} elements
         */
        public List<ExportReference> getExportReferences() {
            return exportReferences;
        }
        
        /**
         * Returns the export reference at the given zero-based index,
         * or {@code null} if the index is out of range.
         *
         * @param index zero-based position of the desired export reference
         * @return the {@link ExportReference} at that index, or {@code null}
         */
        public ExportReference getExportReference(final int index) {
            ExportReference result = null;
            if (index < size()) {
                result = exportReferences.get(index);
            }
            return result;
        }
        
        /** {@inheritDoc} */
        @Override
        public SourceLocation getSourceLocation() {
            return sourceLocation;
        }

        /** {@inheritDoc} */
        @Override
        public void setSourceLocation(final SourceLocation sourceLocation) {
            this.sourceLocation = sourceLocation;
        }
        
        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            return Objects.hash(exportReferences, sourceLocation);
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(final Object object) {
            if (object instanceof AstNode node) {
                return equalsNode(node) 
                          && Objects.equals(sourceLocation, node.getSourceLocation());
            }
            return false;
        }
        
        /** {@inheritDoc} */
        @Override
        public boolean equalsNode(final AstNode obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ExportBlock other = (ExportBlock) obj;
            return Objects.equals(exportReferences, other.exportReferences);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(ExportBlock.class.getSimpleName());
            result.append(" [exportReferences=");
            result.append(exportReferences.size());
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }
        
    }

    /**
     * Represents a single named export reference, optionally aliased, inside an
     * {@link ExportBlock}.  Corresponds to an entry such as {@code foo} or
     * {@code foo as bar} in {@code export { foo, foo as bar }}.
     */
    @JsonPropertyOrder({"name", "alias", "sourceLocation"})
    public static class ExportReference implements JavascriptNode {

        private static final long serialVersionUID = 1L; 
        
        /** The name. */
        @JsonProperty("name") 
        private final Identifier name;
        /** The alias. */
        @JsonProperty("alias") 
        private final Identifier alias;
        
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        
        /** The sourceLocation. */
        @JsonProperty("sourceLocation") 
        private SourceLocation sourceLocation;
        
        /**
         * Constructs an {@code ExportReference} without source-location information.
         *
         * @param name  the exported identifier
         * @param alias the optional alias identifier, or {@code null} if absent
         */
        public ExportReference(final Identifier name,
                               final Identifier alias) {
            this(name, alias, null);
        }

        /**
         * Constructs an {@code ExportReference} with full source-location information.
         *
         * @param name           the exported identifier
         * @param alias          the optional alias identifier, or {@code null} if absent
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public ExportReference(@JsonProperty("name") final Identifier name,
                               @JsonProperty("alias") final Identifier alias,
                               @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.name = name;
            this.alias = alias;
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
         public List<AstNode> astChildren() {
             return AstNode.childrenAttributes(name, alias);
         }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }
        
        /**
         * Sets the parent AST node of this export reference.
         *
         * @param parent the parent {@link AstNode}
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the exported identifier.
         *
         * @return the name of the export
         */
        public Identifier getName() {
            return name;
        }

        /**
         * Returns {@code true} if this export reference has an alias.
         *
         * @return {@code true} when an alias is present, {@code false} otherwise
         */
        public boolean hasAlias() {
            return alias != null;
        }

        /**
         * Returns the alias identifier, or {@code null} if no alias is defined.
         *
         * @return the alias, or {@code null}
         */
        public Identifier getAlias() {
            return alias;
        }

        /** {@inheritDoc} */
        @Override
        public SourceLocation getSourceLocation() {
            return sourceLocation;
        }

        /** {@inheritDoc} */
        @Override
        public void setSourceLocation(final SourceLocation sourceLocation) {
            this.sourceLocation = sourceLocation;
        }

        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            return Objects.hash(alias, name, sourceLocation);
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(final Object object) {
            if (object instanceof AstNode node) {
                return equalsNode(node) 
                          && Objects.equals(sourceLocation, node.getSourceLocation());
            }
            return false;
        }
        
        /** {@inheritDoc} */
        @Override
        public boolean equalsNode(final AstNode obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ExportReference other = (ExportReference) obj;
            return Objects.equals(alias, other.alias)
                    && Objects.equals(name, other.name);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(Export.class.getSimpleName());
            result.append(".");
            result.append(ExportReference.class.getSimpleName());
            result.append(" [name=");
            result.append(name);
            result.append(", alias=");
            result.append(alias);
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }
       
    }
    
    /**
     * Represents a namespace re-export with an optional alias,
     * e.g. {@code export * from './module'} or {@code export * as ns from './module'}.
     */
    @JsonPropertyOrder({"moduleName", "alias", "sourceLocation"})
    public static class AllFrom implements JavascriptNode {

        private static final long serialVersionUID = 1L; 
        
        /** The moduleName. */
        @JsonProperty("moduleName") 
        private final Literal moduleName;
        /** The alias. */
        @JsonProperty("alias") 
        private final Identifier alias;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation") 
        private SourceLocation sourceLocation;

        /**
         * Constructs an {@code AllFrom} node without source location information.
         *
         * @param moduleName the literal representing the source module path
         * @param alias      the optional namespace alias identifier, or {@code null}
         */
        public AllFrom(final Literal moduleName,
                       final Identifier alias) {
            this(moduleName, alias, null);
        }

        /**
         * Constructs an {@code AllFrom} node with full source location information.
         *
         * @param moduleName     the literal representing the source module path
         * @param alias          the optional namespace alias identifier, or {@code null}
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public AllFrom(@JsonProperty("moduleName") final Literal moduleName,
                       @JsonProperty("alias") final Identifier alias,
                       @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.moduleName = moduleName;
            this.alias = alias;
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }

        /** {@inheritDoc} */
        @Override
         public List<AstNode> astChildren() {
             return AstNode.childrenAttributes(moduleName, alias);
         }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }
        
        /**
         * Sets the parent node of this {@code AllFrom} node in the AST.
         *
         * @param parent the parent node to associate
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the literal representing the source module path.
         *
         * @return the module name literal
         */
        public Literal getModuleName() {
            return moduleName;
        }

        /**
         * Returns {@code true} if this re-export has a namespace alias
         * ({@code export * as alias from ...}).
         *
         * @return {@code true} when an alias is present
         */
        public boolean hasAlias() {
            return alias != null;
        }

        /**
         * Returns the namespace alias identifier, or {@code null} if no alias is defined.
         *
         * @return the alias identifier, or {@code null}
         */
        public Identifier getAlias() {
            return alias;
        }
        
        /** {@inheritDoc} */
        @Override
        public SourceLocation getSourceLocation() {
            return sourceLocation;
        }

        /** {@inheritDoc} */
        @Override
        public void setSourceLocation(final SourceLocation sourceLocation) {
            this.sourceLocation = sourceLocation;
        }
        
        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            return Objects.hash(alias, moduleName, sourceLocation);
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(final Object object) {
            if (object instanceof AstNode node) {
                return equalsNode(node) 
                          && Objects.equals(sourceLocation, node.getSourceLocation());
            }
            return false;
        }
        
        /** {@inheritDoc} */
        @Override
        public boolean equalsNode(final AstNode obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final AllFrom other = (AllFrom) obj;
            return Objects.equals(alias, other.alias)
                    && Objects.equals(moduleName, other.moduleName);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(Export.class.getSimpleName());
            result.append(".");
            result.append(AllFrom.class.getSimpleName());
            result.append(" [moduleName=");
            result.append(moduleName);
            result.append(", alias=");
            result.append(alias);
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }
        
    }
    
    /**
     * Represents a re-export from a named module with an explicit export block,
     * e.g. {@code export { foo, bar } from './module'}.
     */
    @JsonPropertyOrder({"moduleName", "exportBlock", "sourceLocation"})
    public static class From implements JavascriptNode {

        private static final long serialVersionUID = 1L; 
        
        /** The moduleName. */
        @JsonProperty("moduleName") 
        private final Literal moduleName;
        /** The exportBlock. */
        @JsonProperty("exportBlock") 
        private final ExportBlock exportBlock;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation") 
        private SourceLocation sourceLocation;
        
        /**
         * Constructs a {@code From} node without source-location information.
         *
         * @param moduleName  the literal representing the source module path
         * @param exportBlock the block of named exports to re-export
         */
        public From(final Literal moduleName,
                    final ExportBlock exportBlock) {
            this(moduleName, exportBlock, null);
        }

        /**
         * Constructs a {@code From} node with full source-location information.
         *
         * @param moduleName     the literal representing the source module path
         * @param exportBlock    the block of named exports to re-export
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public From(@JsonProperty("moduleName") final Literal moduleName,
                    @JsonProperty("exportBlock") final ExportBlock exportBlock,
                    @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.moduleName = moduleName;
            this.exportBlock = exportBlock;
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }
        
        /** {@inheritDoc} */
        @Override
         public List<AstNode> astChildren() {
             return AstNode.childrenAttributes(moduleName, exportBlock);
         }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }
        
        /**
         * Sets the parent AST node of this {@code From} node.
         *
         * @param parent the parent {@link AstNode}
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the literal representing the source module path.
         *
         * @return the module name literal
         */
        public Literal getModuleName() {
            return moduleName;
        }

        /**
         * Returns the export block containing the named exports to re-export.
         *
         * @return the {@link ExportBlock}
         */
        public ExportBlock getExportBlock() {
            return exportBlock;
        }
        
        /** {@inheritDoc} */
        @Override
        public SourceLocation getSourceLocation() {
            return sourceLocation;
        }

        /** {@inheritDoc} */
        @Override
        public void setSourceLocation(final SourceLocation sourceLocation) {
            this.sourceLocation = sourceLocation;
        }
        
        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            return Objects.hash(exportBlock, moduleName, sourceLocation);
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(final Object object) {
            if (object instanceof AstNode node) {
                return equalsNode(node) 
                          && Objects.equals(sourceLocation, node.getSourceLocation());
            }
            return false;
        }
        
        /** {@inheritDoc} */
        @Override
        public boolean equalsNode(final AstNode obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final From other = (From) obj;
            return Objects.equals(exportBlock, other.exportBlock)
                    && Objects.equals(moduleName, other.moduleName);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(Export.class.getSimpleName());
            result.append(".");
            result.append(AllFrom.class.getSimpleName());
            result.append(" [moduleName=");
            result.append(moduleName);
            result.append(", exportBlock=");
            result.append(exportBlock.size());
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }
        
    }
    
}
