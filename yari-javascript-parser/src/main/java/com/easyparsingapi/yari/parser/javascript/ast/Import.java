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
 * Represents a JavaScript ES-module {@code import} declaration in the AST.
 * Captures the module path ({@link Literal}), an optional default import name,
 * an optional namespace alias ({@code import * as alias}), and an optional
 * named-import block ({@link ImportBlock}).
 */
@JsonPropertyOrder({"moduleName", "defaultName", "moduleName", "importBlock", "sourceLocation"})
public class Import implements JavascriptNode {

    private static final long serialVersionUID = 1L; 
    
    /** The moduleName. */
    @JsonProperty("moduleName") 
    private final Literal moduleName;
    /** The defaultName. */
    @JsonProperty("defaultName") 
    private final Identifier defaultName;
    /** The alias. */
    @JsonProperty("alias") 
    private final Identifier alias;
    /** The importBlock. */
    @JsonProperty("importBlock") 
    private final ImportBlock importBlock;
    /** The parent. */
    @JsonIgnore
    private AstNode parent;
    /** The sourceLocation. */
    @JsonProperty("sourceLocation") 
    private SourceLocation sourceLocation;
    
    /**
     * Constructs an {@code Import} declaration without source-location information.
     *
     * @param moduleName  the module path literal
     * @param defaultName the default import identifier, or {@code null}
     * @param alias       the namespace alias identifier ({@code * as alias}), or {@code null}
     * @param importBlock the named-import block, or {@code null}
     */
    public Import(final Literal moduleName,
                  final Identifier defaultName,
                  final Identifier alias,
                  final ImportBlock importBlock) {
        this(moduleName, defaultName, alias, importBlock, null);
    }

    /**
     * Constructs an {@code Import} declaration with full source-location information.
     *
     * @param moduleName     the module path literal
     * @param defaultName    the default import identifier, or {@code null}
     * @param alias          the namespace alias identifier ({@code * as alias}), or {@code null}
     * @param importBlock    the named-import block, or {@code null}
     * @param sourceLocation the source location of this node, or {@code null}
     */
    @JsonCreator
    public Import(@JsonProperty("moduleName") final Literal moduleName,
                  @JsonProperty("defaultName") final Identifier defaultName,
                  @JsonProperty("alias") final Identifier alias,
                  @JsonProperty("importBlock") final ImportBlock importBlock,
                  @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
        super();
        this.moduleName = moduleName;
        this.defaultName = defaultName;
        this.alias = alias;
        this.importBlock = importBlock;
        this.sourceLocation = sourceLocation;
        JavascriptUtil.setAstParent(this);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<AstNode> astChildren() {
        return AstNode.childrenAttributes(moduleName, defaultName, alias, importBlock);
    }
    
    /** {@inheritDoc} */
    @Override
    public AstNode astParent() {
        return parent;
    }

    /**
     * Sets the parent AST node of this import declaration.
     *
     * @param parent the parent {@link AstNode}
     */
    protected void astParent(final AstNode parent) {
        this.parent = parent;
    }

    /**
     * Returns the literal representing the path of the imported module.
     *
     * @return the module name literal
     */
    public Literal getModuleName() {
        return moduleName;
    }

    /**
     * Returns {@code true} if this import has a default binding
     * ({@code import name from '...'}).
     *
     * @return {@code true} when a default import name is present
     */
    public boolean hasDefault() {
        return defaultName != null;
    }

    /**
     * Returns the default import identifier, or {@code null} if this import
     * has no default binding.
     *
     * @return the default-import {@link Identifier}, or {@code null}
     */
    public Identifier getDefaultName() {
        return defaultName;
    }

    /**
     * Returns {@code true} if this import has a namespace alias
     * ({@code import * as alias from '...'}).
     *
     * @return {@code true} when a namespace alias is present
     */
    public boolean hasAlias() {
        return alias != null;
    }

    /**
     * Returns the namespace alias identifier ({@code import * as alias}),
     * or {@code null} if this import has no namespace alias.
     *
     * @return the alias {@link Identifier}, or {@code null}
     */
    public Identifier getAlias() {
        return alias;
    }

    /**
     * Returns {@code true} if this import has a named-import block
     * ({@code import { foo, bar } from '...'}).
     *
     * @return {@code true} when an import block is present
     */
    public boolean hasImportBlock() {
        return importBlock != null;
    }

    /**
     * Returns the named-import block of this declaration, or {@code null} if absent.
     *
     * @return the {@link ImportBlock}, or {@code null}
     */
    public ImportBlock getImportBlock() {
        return importBlock;
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
        return Objects.hash(alias, defaultName, importBlock, moduleName, sourceLocation);
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
        final Import other = (Import) obj;
        return Objects.equals(alias, other.alias) 
                && Objects.equals(defaultName, other.defaultName)
                && Objects.equals(importBlock, other.importBlock) 
                && Objects.equals(moduleName, other.moduleName);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(Import.class.getSimpleName());
        result.append(" [moduleName=");
        result.append(moduleName);
        result.append(", defaultName=");
        result.append(defaultName);
        result.append(", alias=");
        result.append(alias);
        result.append(", importBlock=");
        result.append(importBlock);
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
     * Represents the named-import block ({@code { foo, bar as baz }}) of an
     * {@link Import} declaration.  Contains an ordered list of {@link ImportReference}s.
     */
    @JsonPropertyOrder({"importReferences", "sourceLocation"})
    public static class ImportBlock implements JavascriptNode {

        private static final long serialVersionUID = 1L; 
        
        /** The importReferences. */
        @JsonProperty("importReferences") 
        private final List<ImportReference> importReferences;
        /** The parent. */
        @JsonIgnore
        private AstNode parent;
        /** The sourceLocation. */
        @JsonProperty("sourceLocation") 
        private SourceLocation sourceLocation;

        /**
         * Constructs an {@code ImportBlock} without source location information.
         *
         * @param importReferences the list of named import references
         */
        public ImportBlock(final List<ImportReference> importReferences) {
            this(importReferences, null);
        }

        /**
         * Constructs an {@code ImportBlock} with full source location information.
         *
         * @param importReferences the list of named import references
         * @param sourceLocation   the source location of this node, or {@code null}
         */
        @JsonCreator
        public ImportBlock(@JsonProperty("importReferences") final List<ImportReference> importReferences,
                           @JsonProperty("sourceLocation") final SourceLocation sourceLocation) {
            super();
            this.importReferences = CollectionUtil.nullToEmpty(importReferences);
            this.sourceLocation = sourceLocation;
            JavascriptUtil.setAstParent(this);
        }

        /** {@inheritDoc} */
        @Override
        public List<AstNode> astChildren() {
            return AstNode.childrenAttributes(importReferences);
        }
        
        /** {@inheritDoc} */
        @Override
        public AstNode astParent() {
            return parent;
        }

        /**
         * Sets the parent node of this import block in the AST.
         *
         * @param parent the parent node to associate
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the number of named import references in this block.
         *
         * @return the import reference count
         */
        public int size() {
            return importReferences.size();
        }

        /**
         * Returns the list of named import references in this block.
         *
         * @return the list of {@link ImportReference} elements
         */
        public List<ImportReference> getImportReferences() {
            return importReferences;
        }

        /**
         * Returns the import reference at the given zero-based index,
         * or {@code null} if the index is out of range.
         *
         * @param index zero-based position of the desired import reference
         * @return the {@link ImportReference} at that index, or {@code null}
         */
        public ImportReference getImportReference(final int index) {
            ImportReference result = null;
            if (index < size()) {
                result = importReferences.get(index);
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
            return Objects.hash(importReferences, sourceLocation);
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
            final ImportBlock other = (ImportBlock) obj;
            return Objects.equals(importReferences, other.importReferences);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(ImportBlock.class.getSimpleName());
            result.append(" [importReferences=");
            result.append(importReferences.size());
            if (sourceLocation != null) {
                result.append(", sourceLocation=");
                result.append(sourceLocation);
            }
            result.append("]");
            return result.toString();
        }
        
    }

    /**
     * Represents a single named binding inside an {@link ImportBlock},
     * for example {@code foo} or {@code foo as bar}.
     * Holds the exported name and an optional local alias.
     */
    @JsonPropertyOrder({"name", "alias", "sourceLocation"})
    public static class ImportReference implements JavascriptNode {

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
         * Constructs an {@code ImportReference} without source location information.
         *
         * @param name  the exported name being imported
         * @param alias the optional local alias, or {@code null}
         */
        public ImportReference(final Identifier name,
                               final Identifier alias) {
            this(name, alias, null);
        }

        /**
         * Constructs an {@code ImportReference} with full source location information.
         *
         * @param name           the exported name being imported
         * @param alias          the optional local alias, or {@code null}
         * @param sourceLocation the source location of this node, or {@code null}
         */
        @JsonCreator
        public ImportReference(@JsonProperty("name") final Identifier name,
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
         * Sets the parent node of this import reference in the AST.
         *
         * @param parent the parent node to associate
         */
        protected void astParent(final AstNode parent) {
            this.parent = parent;
        }

        /**
         * Returns the exported name that is being imported.
         *
         * @return the name identifier
         */
        public Identifier getName() {
            return name;
        }

        /**
         * Returns {@code true} if this import reference has a local alias
         * ({@code foo as bar}).
         *
         * @return {@code true} when an alias is present
         */
        public boolean hasAlias() {
            return alias != null;
        }

        /**
         * Returns the local alias identifier, or {@code null} if no alias is defined.
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
            final ImportReference other = (ImportReference) obj;
            return Objects.equals(alias, other.alias)
                    && Objects.equals(name, other.name);
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            final StringBuilder result = new StringBuilder();
            result.append(Import.class.getSimpleName());
            result.append(".");
            result.append(ImportReference.class.getSimpleName());
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

}
