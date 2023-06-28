{
  inputs = {
    utils.url = "github:numtide/flake-utils";
    nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
  };
  outputs = { self, nixpkgs, utils }:
    utils.lib.eachDefaultSystem (system:
      let pkgs = import nixpkgs { inherit system; };
      in {
        devShell = with pkgs;
          mkShell {
            buildInputs = [ clojure babashka ];

          };
        packages = rec {
          default = pkgs.stdenv.mkDerivation {
            name = "create-riddle";
            version = "0.1.0";
            src = self;
            buildInputs = [ pkgs.babashka ];
            installPhase = ''
              mkdir -p $out/bin
              cp create-riddle $out/bin/
              cp styles.css $out/bin/
            '';
          };

        };
        apps = rec {
          default = utils.lib.mkApp { drv = self.packages.${system}.default; };
        };
      });
}
