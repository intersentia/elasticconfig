package be.intersentia.elasticsearch.configuration.model;

import be.intersentia.elasticsearch.configuration.annotation.Index;
import be.intersentia.elasticsearch.configuration.annotation.analyzer.*;
import be.intersentia.elasticsearch.configuration.annotation.mapping.*;

import java.util.Date;
@Analysis(
        filters = {
                @Filter(name = "stop_dutch", type = Filter.STOP, properties = {
                        @Property(key = "ignore_case", value = "true"),
                        @Property(key = "stopwords_path", value="dutch.txt")
                }),
                @Filter(name = "stop_french", type = Filter.STOP, properties = {
                        @Property(key = "ignore_case", value = "true"),
                        @Property(key = "stopwords_path", value = "french.txt")
                }),
                @Filter(name = "delimiter_index", type = Filter.WORD_DELIMITER, properties = {
                        @Property(key = "catenate_words", value = "true"),
                        @Property(key = "catenate_numbers", value = "true")
                }),
                @Filter(name = "stem_dutch", type = Filter.STEMMER, properties = @Property(key = "name", value = "dutch")),
                @Filter(name = "stem_french", type = Filter.STEMMER, properties = @Property(key = "name", value = "french")),
                @Filter(name = "synonymSerial", type = Filter.SYNONYM, properties = {
                        @Property(key = "synonyms_path", value = "serial_syn.txt"),
                        @Property(key = "ignore_case", value = "true"),
                        @Property(key = "expand", value = "false")
                }),
                @Filter(name = "synonymPlace", type = Filter.SYNONYM, properties = {
                        @Property(key = "synonyms_path", value = "place_syn.txt"),
                        @Property(key = "ignore_case", value = "true"),
                        @Property(key = "expand", value = "false")
                }),
                @Filter(name = "synonymCourt", type = Filter.SYNONYM, properties = {
                        @Property(key = "synonyms_path", value = "court_syn.txt"),
                        @Property(key = "ignore_case", value = "true"),
                        @Property(key = "expand", value = "false")
                })
        },
        customAnalyzers = {
                @CustomAnalyzer(name = "default", tokenizer = Tokenizer.WHITESPACE, filters = {
                        Filter.ASCII_FOLDING, "stop_dutch", "stop_french", "delimiter_index",
                        Filter.LOWERCASE, "stem_dutch", "stem_french", Filter.PORTER_STEM
                }),
                @CustomAnalyzer(name = "default_search", tokenizer = Tokenizer.WHITESPACE, filters = {
                        Filter.ASCII_FOLDING, "stop_dutch", "stop_french", Filter.WORD_DELIMITER,
                        Filter.LOWERCASE, "stem_dutch", "stem_french", Filter.PORTER_STEM
                }),
                @CustomAnalyzer(name = "reference", tokenizer = Tokenizer.WHITESPACE, filters = {
                        Filter.ASCII_FOLDING, Filter.LOWERCASE, "synonymSerial"
                }),
                @CustomAnalyzer(name = "facet_text", tokenizer = Tokenizer.KEYWORD, filters = {Filter.LOWERCASE, Filter.TRIM}),
                @CustomAnalyzer(name = "no-lowercase_facet_text", tokenizer = Tokenizer.KEYWORD, filters = Filter.TRIM),

                @CustomAnalyzer(name = "spell_index", tokenizer = Tokenizer.WHITESPACE, filters = {
                        "synonymPlace", "synonymCourt", "stop_dutch", "stop_french", "delimiter_index", Filter.LOWERCASE
                }),
                @CustomAnalyzer(name = "spell_search", tokenizer = Tokenizer.WHITESPACE, filters = {
                        "synonymPlace", "synonymCourt", "stop_dutch", "stop_french", Filter.WORD_DELIMITER, Filter.LOWERCASE
                })
        }
)

@Index
public class TestModel {
    @KeywordMapping(index = true, store = true)
    private String documentId;
    @KeywordMapping(index = true, store = true)
    private String page;
    @KeywordMapping(index = false, store = true)
    private String publicationCode;
    @NumericMapping(index = true, store = true, copyTo = "xxx")
    private int volume;
    @KeywordMapping(index = true, store = true)
    private String publicationNumber;
    @DateMapping(index = true, store = true)
    private Date publicationDate;
    @TextMapping(index = true, store = false)
    private String content;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPublicationCode() {
        return publicationCode;
    }

    public void setPublicationCode(String publicationCode) {
        this.publicationCode = publicationCode;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getPublicationNumber() {
        return publicationNumber;
    }

    public void setPublicationNumber(String publicationNumber) {
        this.publicationNumber = publicationNumber;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
