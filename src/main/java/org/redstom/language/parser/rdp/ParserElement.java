package org.redstom.language.parser.rdp;

/**
 * Describes a part of the grammar of the language.
 *
 * @param <R>  The result of this step of the parsing
 * @param <D> Additional data needed to parse this particular element
 * @author RedsTom
 */
@FunctionalInterface
public interface ParserElement<R, D> {

    /**
     * Parses the given context.
     *
     * @param ctx     Parsing context
     * @param c Extra context for the parsing
     * @return The result of the parsing
     */
    R parse(ParseContext ctx, D data);

}
