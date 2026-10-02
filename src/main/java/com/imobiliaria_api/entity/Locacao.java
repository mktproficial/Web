package com.imobiliaria_api.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "locacao")
public class Locacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_imovel", nullable = false)
    private Imovel imovel;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_vencimento")
    private Integer dataVencimento;

    @Column(name = "perc_multa", precision = 5, scale = 2)
    private BigDecimal percMulta;

    @Column(name = "valor_aluguel", precision = 10, scale = 2)
    private BigDecimal valorAluguel;

    @Column(columnDefinition = "TEXT")
    private String obs;

    @OneToMany(mappedBy = "locacao", cascade = CascadeType.ALL)
    private List<Aluguel> alugueis = new ArrayList<>();

    public Locacao() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Integer getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Integer dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public BigDecimal getPercMulta() {
        return percMulta;
    }

    public void setPercMulta(BigDecimal percMulta) {
        this.percMulta = percMulta;
    }

    public BigDecimal getValorAluguel() {
        return valorAluguel;
    }

    public void setValorAluguel(BigDecimal valorAluguel) {
        this.valorAluguel = valorAluguel;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public List<Aluguel> getAlugueis() {
        return alugueis;
    }

    public void setAlugueis(List<Aluguel> alugueis) {
        this.alugueis = alugueis;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Locacao locacao = (Locacao) o;
        return Objects.equals(id, locacao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Locacao{" +
                "id=" + id +
                ", imovel=" + imovel +
                ", cliente=" + cliente +
                ", ativo=" + ativo +
                ", dataFim=" + dataFim +
                ", dataInicio=" + dataInicio +
                ", dataVencimento=" + dataVencimento +
                ", percMulta=" + percMulta +
                ", valorAluguel=" + valorAluguel +
                ", obs='" + obs + '\'' +
                ", alugueis=" + alugueis +
                '}';
    }
}