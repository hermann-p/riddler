{
  inputs = {
    utils.url = "github:numtide/flake-utils";
    nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
  };
  outputs = { self, nixpkgs, utils }:
    utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs { inherit system; };
        cljdeps =
          import ./deps.nix { inherit (pkgs) fetchMavenArtifact fetchgit lib; };
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
            DEPS_CP = cljdeps.makeClasspaths { };
            installPhase = ''
              mkdir -p $out/bin
              cp -r create-riddle bb.edn src $out/bin/
            '';
          };

        };
        apps = rec {
          default = utils.lib.mkApp { drv = self.packages.${system}.default; };
        };
      });
}
