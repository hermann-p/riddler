# Create crossword puzzles from a JSON configuration

Just a short handy CLI tool for me and my family, and an excuse to learn
Babashka.

## Args

- `--file <path-to-json>` use that file as input
- `--url <https://path.to/the.json>` fetch the url and use it as input. Will
  create a local cache in `cwd` to prevent the need to repeatedly fetch.

## Nix flake users

Just use the flake, dudes. Clone and/or locally install to your heart's desire,
or simply

`nix run github:hermann-p/riddler -- [args]`

## Non-nix

### Requirements:

- `make`
- `babashka`

### Build:

- clone the repo
- `make clean && make`

Will produce a `create-riddle` executable that requires the args from above.

# Riddle format

``` json
{
  "questions": [
    { 
      "direction": ["v" "h"],
      "nr": number,           // Together with direction will become "1 horizontal" etc.  
      "question": string,
      "xc": number,           // x-coordinate in the grid
      "yc": number,           // y-coordinate in the grid
      "length": number        // length of the answer
    }
  ]
}
```

`
