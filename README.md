# Env2File - Environment Variable Extractor

`env2file` is a command-line tool that extracts environment variable keys from `.env` files and stores them in a separate file. This helps developers easily identify which environment variables an application depends on.

The tool also provides an option to automatically commit the extracted keys to a Git repository.

## Features

- Extracts environment variable keys from `.env` files.
- Stores the keys in a separate file (e.g., `.env.example`).
- Supports automatic Git integration to commit the extracted keys.
- Built as a native executable using GraalVM for better performance.
- Command-line interface powered by PicoCLI.

## Installation

### Prerequisites

- Java 17 or later (for running the JAR file).
- GraalVM (if using the compiled binary version).
- Git (if using the Git integration feature).

### Download and Install

#### Option 1: Run with Java

```sh
java -jar env2file.jar <path_to_env_file> [output_file]
```

#### Option 2: Use the Compiled Binary (GraalVM Native Image)

Make the binary executable and move it to `/usr/local/bin` for global access:

```sh
chmod +x env2file
sudo mv env2file /usr/local/bin/
```

Now you can run:

```sh
env2file <path_to_env_file> [output_file]
```

## Usage

### Extract Environment Keys to a File

```sh
env2file /path/to/.env .env.example
```

This will extract all environment variable keys from the `.env` file and store them in `.env.example`.

### Automatically Commit Extracted Keys to Git

```sh
env2file /path/to/repo --git
```

This will:

1. Extract environment keys from `.env` files.
2. Store them in `.env.example`.
3. Commit the changes to the Git repository with the message:
   ```
   chore: add environment keys to sample file
   ```

### Automatically Run on `git push` (Pre-Push Hook)

To automate the execution of `env2file` every time you run `git push`, create a Git pre-push hook:

1. Navigate to your Git repository:
   ```sh
   cd /path/to/your/repo
   ```
2. Create a new pre-push hook:
   ```sh
   nano .git/hooks/pre-push
   ```
3. Add the following script to execute `env2file` before pushing:
   ```sh
   #!/bin/bash
   env2file --git /path/to/repoo
   ```
4. Save the file and give it executable permissions:
   ```sh
   chmod +x .git/hooks/pre-push
   ```

Now, every time you run `git push`, the tool will extract the environment keys, update `.env.example`, and commit the changes automatically.

### Display Help

```sh
env2file --help
```

## Example

```sh
$ env2file .env .env.example
Wrote 10 environment keys to .env.example
```

## Contributing

Feel free to open issues or submit pull requests if you have improvements or suggestions.

