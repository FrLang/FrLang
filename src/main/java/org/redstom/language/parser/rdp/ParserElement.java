package org.redstom.language.parser.rdp;

/**
 * Describes a part of the grammar of the language.
 *
 * @param <RESULT>  The result of this step of the parsing
 * @param <CONTEXT> Additional data needed to parse this particular element
 * @author RedsTom
 */
@FunctionalInterface
public interface ParserElement<RESULT, CONTEXT> {

    /**
     * Parses the given context.
     *
     * @param ctx     Parsing context
     * @param context Extra context for the parsing
     * @return The result of the parsing
     */
    RESULT parse(ParseContext ctx, CONTEXT context);

}
