# oreilly-obsidian-importer

Imports CSV file from Safari/O'Reilly that contains highlights and notes to 
a series of Markdown files that are compatible with [Obsidian](https://obsidian.md)

## Key Features

1. Creates a directory per book title (as specified in the CSV file)
2. Within each book's directory
    1. Creates a `<Book Title>.md` file that contains a link to the book's webpage
    2. Creates a separate Markdown file for each highlight/note based on the 
       entry's URL (to ensure unique filenames)
        1. Each highlight's Markdown file contains an 
           [Obsidian backlink](https://help.obsidian.md/How+to/Working+with+backlinks)
           to the main page for the book

## Installation

1. Copy the `ori` binary to your path
2. Setup permissions: `sudo xattr -r -d com.apple.quarantine ori`

## Usage

1. Go to [Safari/O'Reilly](https://learning.oreilly.com/home/)
2. Select *Highlights* under *Your O'Reilly* (top right) and download CSV file
3. `path/to/ori <path to csv file> <directory>`

Within the specified directory, ori will create a directory per book and a 
Markdown file per highlight/note

## License

Copyright © 2021 Andrew Nguyen

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at T

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.