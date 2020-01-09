package com.mygdx.game.name;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum NameTheme {
    FANTASY(Arrays.asList("aith", "fay", "illi", "alo", "srin", "fol", "mon", "lin","ai", "kii", "rion", "tyr"), Arrays.asList("white", "hill", "bloom","silver", "strong", "glow", "peak", "stride", "rider", "shade"), Arrays.asList("son", "li", "ssen", "kor")),
//    FUTURE(Arrays.asList("mon", "fay", "shi", "zag", "blarg", "rash", "izen"), Arrays.asList("malo", "zak", "abo", "wonk"), Arrays.asList("son", "li", "ssen", "kor")),
//    SCIENTIFIC(Arrays.asList("mon", "fay", "shi", "zag", "blarg", "rash", "izen"), Arrays.asList("malo", "zak", "abo", "wonk"), Arrays.asList("son", "li", "ssen", "kor")),
    DARK(Arrays.asList("vic", "yul", "temp", "rass", "tor", "sey", "dra"), Arrays.asList("torn", "mort", "crypt", "borg", "lob", "will", "lab"), Arrays.asList("lit", "dak", "nor", "fin", "det")),
    NORDIC(Arrays.asList("arulf", "eil", "har", "mar", "ene", "orvo", "klar"), Arrays.asList("askel", "fred", "fri", "lund", "eskel", "berg", "hat"), Arrays.asList("son", "ver", "del", "xen", "stad", "kki", "nen", "dot")),
    GREENLANDIC(Arrays.asList("arulf", "eil", "har", "mar", "ene", "orvo", "klar"), Arrays.asList("askel", "fred", "fri", "lund", "eskel", "berg", "hat"), Arrays.asList("aka", "laaq", "luk", "mineq", "nguujuk", "nnguaq", "paluk", "pik", "raq", "rsuk", "suk")),
    GERMAN(Arrays.asList("gei", "sler", "hei", "der", "nich"), Arrays.asList("weis", "man", "fri", "ler", "him", "berg", "quist"), Arrays.asList("chen", "ilo", "ing", "k", "ka", "ke", "ken", "ko")),
    CZECH(Arrays.asList("pav", "jako", "buk", "dan", "mil", "neco"), Arrays.asList("pra", "fred", "fri", "lund", "zum", "nov", "vam"), Arrays.asList("cky", "cka", "cek", "cik", "ar", "ak")),
    ;

    private List<String> firstNameSyllables;
    private List<String> lasNameSyllables;
    private List<String> nameSuffixes;

}
