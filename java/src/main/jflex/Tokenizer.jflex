package com.searchengine.indexer;
//Hayden Prater Information Retrieval Hw4
//10/25/2023
//----------------------------------------
// Filename:  tokenizer-2.jflex
// To compile: jflex tokenizer-2.jflex
//             javac Lexer.java
//             javac Main.java
//             javac Tokens.java
//             java -cp . Main inputDir outputDir
//     OR - Run from build script
//             ./tokenizer-2.sh inputDir outputDir
//----------------------------------------

import java.io.*;
import com.searchengine.model.Document;
import com.searchengine.model.Query;

%%

%class Lexer
%public
%unicode
%line
%column

%{
    private Document document;
    private Query query;

    // Constructor
    public Lexer(Reader reader, Document document, Query query) {
        this.yyreset(reader);
        this.document = document;
        this.query = query;
    }

%}

%type String

// Rules used for tokens (Punctuation will be removed by the .)

WORD = [A-Za-z]+
LETTER = [A-Za-z]
FLOAT = [0-9]*\.[0-9]+
INTEGERS = 0|([1-9][0-9]*)
EMAIL = [A-Za-z0-9_.-]+@[A-Za-z0-9.-]+\.[A-Za-z0-9_-]{2,4}
URL = (https?|ftp):\/\/[-\w]+(\.\w[-\w]*)+(:\d+)?(\/\S*?)
ATTRIBUTE = [\n\t]+(([A-Za-z\-_]+)?[ \n\t]*=?[ \n\t]*((\"[^\"]*\")|([A-Za-z0-9]+)|({URL}))[ \n\t]*)+[ \n\t]*
STARTTAG = <\!?[A-Za-z0-9]+{ATTRIBUTE}*[\/]?>
ENDTAG = <[\/][A-Za-z0-9]+>
PHONENUMBER = [0-9]{3}-[0-9]{3}-[0-9]{4}
%%

//Rules used for what to do with a token
[\s\n\t\r]		    {}
{EMAIL}             { if (document != null) {
                              document.incrementTotalTokens();
                              document.addToDocumentHashtable(yytext());
                          } else {
                              query.addToTokenizedQueryList(yytext());
                          } }
{URL}               { if (document != null) {
                               document.incrementTotalTokens();
                               document.addToDocumentHashtable(yytext());
                           } else {
                               query.addToTokenizedQueryList(yytext());
                           } }
{INTEGERS}          { if (document != null) {
                               document.incrementTotalTokens();
                               document.addToDocumentHashtable(yytext());
                           } else {
                                query.addToTokenizedQueryList(yytext());
                           } }
{FLOAT}             {}
{PHONENUMBER}       { if (document != null) {
                                document.incrementTotalTokens();
                                document.addToDocumentHashtable(yytext());
                           } else {
                                 query.addToTokenizedQueryList(yytext());
                           } }
{STARTTAG}          {}
{ENDTAG}            {}
{LETTER}            {}
{WORD}              {if (document != null) {
                                if(!document.isStopOrSingleLetterWord(yytext())){
                                    document.incrementTotalTokens();
                                    document.addToDocumentHashtable(yytext());
                               }
                          } else {
                               query.addToTokenizedQueryList(yytext());
                         } }
.		            {}


