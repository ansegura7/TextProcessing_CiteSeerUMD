# Text Processing: CiteSeer UMD

## Author Info
- Created By: Andres Segura Tinoco
- Created On: June 9, 2018

## Program Description
The program has 5 classes. Each of them is described below:

File class: **MainClass.java**
- Description: Main class of the program. Receive the directory path where the text files are located. It should only be executed and all the answers will be obtained.
- Main methods:
    - **main**: Main function of main class
    - **preProcessingFiles**: Function that pre-processing the content of a collection of documents.
    - **createTFIDFoutput**: Save into text files the output of the TF-IDF
  
File class: **TextPreprocessing.java**
- Description: Class that contains the main functions to pre-process the text and build the inverted index.
- Main methods:
    - **getCollection**: Function that a collection of documents from a file list.
    - **createInverseIndex**: Function that create a inverted index from a collection of documents.
    - **getWordsFrequency**: Calculate the total frequency of words in the document collection.
    - **createSparseTFIDF**: Function that create the sparse TF-IDF representation of collection of documents.
    - **getOrdedWordList**: Sorting words alphabetically.
    - **getWordCount**: Summarize the total number of words in the collection (file by file).
    - **cleanText**: Clean raw text. Remove punctuation. Data Quality function. Step 1: Transform to lower case. Step 2: Remove punctuation symbols. Step 3: Remove digits.

File class: **Reader.java**
- Description: Class that contains the functions for reading text files.
- Main methods:
    - **readFile**: Read a file line by line and return an array of strings.
    - **readFiles**: Read the document collection and returns a hash table.
    - **getFileList**: Returns the file list (of file name) from a directory.
    - **readFileContent**: Read and return the file content.
    - **saveArrayList**: Create a text file from a Array List of Strings.

File class: **Utilities.java**
- Description: Class that contains generic or utility functions for any program.
- Main methods:
    - **filepathExists**: Returns True if the directory path exists. Else return False.
    - **fileExists**: Returns True if the file path exists. Else return False.
    - **splitTextByTokens**: Split a raw text by tokens. Tokenize on whitespace.
    - **sortHashtableByValue**: Sort a Hash table by numeric values.
    - **getStopWordList**: Return a list of stop words.

File class: **Porter.java**
- Description: Compile it, import the Porter class into you program and create an instance. Then use the stripAffixes method of this method which takes a String as input and returns the stem of this String again as a String.
- Main methods:
    - **stripAffixes**: makes the steeming of token.