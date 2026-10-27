package com.imobiliaria_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name="imoveis")
public class Imovel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "tipo_imovel", nullable = false, length = 50)
    private String tipo_imovel;

    @NotBlank
    @Size(min = 2, max = 255)
    @Column(name = "endereco", nullable = false, length = 255)
    private String endereco;

    @NotBlank
    @Size(min = 8, max = 8)
    @Pattern(regexp = "\\d{8}")
    @Column(name = "cep")
    private String cep;

    @NotNull
    @Min(value = 0)
    @Column(name = "dormitorios")
    private Integer dormitorios;

    @NotNull
    @Min(value = 0)
    @Column(name = "banheiros")
    private Integer banheiros;

    @NotNull
    @Min(value = 0)
    @Column(name = "suites")
    private Integer suites;

    @NotNull
    @Min(value = 1)
    @Column(name = "metragem")
    private Integer metragem;

    @Min(value = 1)
    @Column(name = "valor_aluguel_sug", precision = 10, scale = 2)
    private BigDecimal valorAluguelSug;

    @Column(name = "obs", columnDefinition = "TEXT")
    private String obs;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo_imovel() {
        return tipo_imovel;
    }

    public void setTipo_imovel(String tipo_imovel) {
        this.tipo_imovel = tipo_imovel;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getDormitorios() {
        return dormitorios;
    }

    public void setDormitorios(Integer dormitorios) {
        this.dormitorios = dormitorios;
    }

    public Integer getBanheiros() {
        return banheiros;
    }

    public void setBanheiros(Integer banheiros) {
        this.banheiros = banheiros;
    }

    public Integer getSuites() {
        return suites;
    }

    public void setSuites(Integer suites) {
        this.suites = suites;
    }

    public Integer getMetragem() {
        return metragem;
    }

    public void setMetragem(Integer metragem) {
        this.metragem = metragem;
    }

    public BigDecimal getValorAluguelSug() {
        return valorAluguelSug;
    }

    public void setValorAluguelSug(BigDecimal valorAluguelSug) {
        this.valorAluguelSug = valorAluguelSug;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Imovel imovel = (Imovel) o;
        return Objects.equals(id, imovel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Imovel{" +
                "id=" + id +
                ", tipo_imovel='" + tipo_imovel + '\'' +
                ", endereco='" + endereco + '\'' +
                ", cep='" + cep + '\'' +
                ", dormitorios=" + dormitorios +
                ", banheiros=" + banheiros +
                ", suites=" + suites +
                ", metragem=" + metragem +
                ", valorAluguelSug=" + valorAluguelSug +
                ", obs='" + obs + '\'' +
                '}';
    }
}
