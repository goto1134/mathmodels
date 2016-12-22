package goto1134.mathmodels.tube;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationFamily;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Created by Andrew
 * on 18.12.2016.
 */
public class TubeFrameTest {
    @Test
    public void getTheory() {
        Parser parser = Parser.builder().build();
        Node document = parser.parse("This is *Sparta*");
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        Assert.assertEquals("ss", renderer.render(document));
    }

    public void test() throws IOException {
        MutableDataHolder options = new MutableDataSet();

        // configure for MultiMarkdown differences from FixedIndent family
        options.setFrom(ParserEmulationFamily.FIXED_INDENT.getOptions()
                .setAutoLoose(true)
                .setAutoLooseOneLevelLists(true)
                .setItemMarkerSpace(false)
                .setLooseWhenBlankFollowsItemParagraph(true)
                .setLooseWhenHasTrailingBlankLine(false))

                // Other compatibility options, outside of lists
                .set(HtmlRenderer.RENDER_HEADER_ID, true)
                .set(HtmlRenderer.HEADER_ID_GENERATOR_RESOLVE_DUPES, false)
                .set(HtmlRenderer.HEADER_ID_GENERATOR_TO_DASH_CHARS, "")
                .set(HtmlRenderer.HEADER_ID_GENERATOR_NO_DUPED_DASHES, true)
                .set(HtmlRenderer.SOFT_BREAK, " ");

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        String input = new String(Files.readAllBytes(Paths.get("theory/tube_theory_ru.md")));

        Node document = parser.parse(input);
        renderer.render(document);  // "<p>This is <em>Sparta</em></p>\n"
    }

}