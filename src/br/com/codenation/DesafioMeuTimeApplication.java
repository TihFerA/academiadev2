package br.com.codenation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import br.com.codenation.desafio.annotation.Desafio;
import br.com.codenation.desafio.app.MeuTimeInterface;
import br.com.codenation.desafio.exceptions.CapitaoNaoInformadoException;
import br.com.codenation.desafio.exceptions.IdentificadorUtilizadoException;
import br.com.codenation.desafio.exceptions.JogadorNaoEncontradoException;
import br.com.codenation.desafio.exceptions.TimeNaoEncontradoException;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class DesafioMeuTimeApplication implements MeuTimeInterface {

    private ArrayList<Time> time = new ArrayList<>();
    private ArrayList<Jogador> jogador = new ArrayList<>();
    Long idJogador;
    String nomeJogador;
    Boolean hasTime;
    Boolean hasJogador;
    String nomeTime;
    Integer melhorIndice;
    BigDecimal Salario;
    Integer contador;
    String corCamisaPrincipalCasa;
    Integer idade;
    Long idTime;
    String corCamisaPrincipalFora;
    String corCamisaSecundariaFora;

    @Desafio("incluirTime")
    public void incluirTime(Long id, String nome, LocalDate dataCriacao, String corUniformePrincipal, String corUniformeSecundario) {
        this.time.forEach(t -> {
            if (t.getId().equals(id)) {
                throw new IdentificadorUtilizadoException();
            }
        });

        Time time = new Time();
        time.setId(id);
        time.setNome(nome);
        time.setDataCriacao(dataCriacao);
        time.setCorUniformePrincipal(corUniformePrincipal);
        time.setCorUniformeSecundario(corUniformeSecundario);
        this.time.add(time);

    }

    @Desafio("incluirJogador")
    public void incluirJogador(Long id, Long idTime, String nome, LocalDate dataNascimento, Integer nivelHabilidade, BigDecimal salario) {
        this.jogador.forEach(j -> {
            if (j.getId().equals(id)) {
                throw new IdentificadorUtilizadoException();
            }
        });

        this.hasTime = false;
        this.time.forEach(t -> {
            if (t.getId().equals(idTime)) {
                this.hasTime = true;
            }
        });

        if (!this.hasTime) {
            throw new TimeNaoEncontradoException();
        }

        Jogador jogador = new Jogador();
        jogador.setId(id);
        jogador.setIdTime(idTime);
        jogador.setNome(nome);
        jogador.setDataNascimento(dataNascimento);
        jogador.setNivelHabilidade(nivelHabilidade);
        jogador.setSalario(salario);
        this.jogador.add(jogador);

    }

    @Desafio("definirCapitao")
    public void definirCapitao(Long idJogador) {
        this.hasJogador = false;
        this.idTime = new Long(0);
        this.jogador.forEach(j -> {
            if (j.getId().equals(idJogador)) {
                this.hasJogador = true;
                j.setCapitao(true);
                this.idTime = j.getIdTime();
            }
        });

        if (!this.hasJogador) {
            throw new JogadorNaoEncontradoException();
        }

        this.jogador.forEach(j -> {
            if (j.getIdTime().equals(this.idTime) && !j.getId().equals(idJogador)) {
                j.setCapitao(false);
            }
        });
    }

    @Desafio("buscarCapitaoDoTime")
    public Long buscarCapitaoDoTime(Long idTime) {
        this.idJogador = new Long(0);
        this.hasTime = false;
        this.time.forEach(t -> {
            if (t.getId().equals(idTime)) {
                this.hasTime = true;
                this.jogador.forEach(j -> {
                    if (j.getIdTime().equals(t.getId())) {
                        if (j.getCapitao()) {
                            this.idJogador = j.getId();
                        }
                    }
                });
            }
        });

        if (!this.hasTime) {
            throw new TimeNaoEncontradoException();
        }

        if (idJogador > 0) {
            return this.idJogador;
        }
        throw new CapitaoNaoInformadoException();
    }

    @Desafio("buscarNomeJogador")
    public String buscarNomeJogador(Long idJogador) {
        this.nomeJogador = null;
        this.hasJogador = false;
        this.jogador.forEach(j -> {
            if (j.getIdTime().equals(idJogador)) {
                this.hasJogador = true;
                this.nomeJogador = j.getNome();
            }
        });

        if (!this.hasJogador) {
            throw new JogadorNaoEncontradoException();
        }
        return this.nomeJogador;
    }

    @Desafio("buscarNomeTime")
    public String buscarNomeTime(Long idTime) {
        this.nomeTime = null;
        this.hasTime = false;
        this.time.forEach(t -> {
            if (t.getId().equals(idTime)) {
                this.hasTime = true;
                this.nomeTime = t.getNome();
            }
        });

        if (!this.hasTime) {
            throw new TimeNaoEncontradoException();
        }
        return this.nomeTime;
    }

    @Desafio("buscarJogadoresDoTime")
    public List<Long> buscarJogadoresDoTime(Long idTime) {
        List<Long> jogadores = new ArrayList<>();
        this.hasTime = false;
        this.time.forEach(t -> {
            if (t.getId().equals(idTime)) {
                this.hasTime = true;
                this.jogador.forEach(j -> {
                    if (j.getIdTime().equals(t.getId())) {
                        jogadores.add(j.getId());
                    }
                });
            }
        });

        if (!this.hasTime) {
            throw new UnsupportedOperationException();
        }
        Collections.sort(jogadores);
        return jogadores;
    }

    @Desafio("buscarMelhorJogadorDoTime")
    public Long buscarMelhorJogadorDoTime(Long idTime) {
        this.melhorIndice = 0;
        this.hasTime = false;
        this.time.forEach(t -> {
            if (t.getId().equals(idTime)) {
                this.hasTime = true;
                this.jogador.forEach(j -> {
                    if (j.getIdTime().equals(t.getId())) {
                        if (j.getNivelHabilidade() > this.melhorIndice) {
                            this.idJogador = j.getId();
                            this.melhorIndice = j.getNivelHabilidade();
                        } else if (j.getNivelHabilidade().equals(this.melhorIndice)) {
                            if (j.getId() < this.idJogador) {
                                this.idJogador = j.getId();
                            }
                        }
                    }
                });
            }
        });

        if (!this.hasTime) {
            throw new TimeNaoEncontradoException();
        }

        return this.idJogador;
    }

    @Desafio("buscarJogadorMaisVelho")
    public Long buscarJogadorMaisVelho(Long idTime) {
        this.idade = 0;
        this.idJogador = new Long(0);
        this.hasTime = false;

        this.jogador.forEach(j -> {
            if (j.getIdTime().equals(idTime)) {
                this.hasTime = true;
                Integer age = Period.between(j.getDataNascimento(), LocalDate.now()).getYears();
                if (age > this.idade) {
                    this.idade = age;
                    this.idJogador = j.getId();
                } else if (Objects.equals(age, this.idade) && this.idJogador > j.getId()) {
                    this.idJogador = j.getId();
                }
            }
        });

        if (!this.hasTime) {
            throw new TimeNaoEncontradoException();
        }

        return this.idJogador;
    }

    @Desafio("buscarTimes")
    public List<Long> buscarTimes() {
        List<Long> times = new ArrayList<>();
        this.time.forEach(t -> {
            times.add(t.getId());
        });
        Collections.sort(times);
        return times;
    }

    @Desafio("buscarJogadorMaiorSalario")
    public Long buscarJogadorMaiorSalario(Long idTime) {
        this.Salario = new BigDecimal(0);
        this.idJogador = new Long(0);
        this.time.forEach(t -> {
            if (t.getId().equals(idTime)) {
                this.hasTime = true;
                this.jogador.forEach(j -> {
                    if (j.getIdTime().equals(t.getId())) {
                        if (j.getSalario().compareTo(this.Salario) == 1) {
                            this.Salario = j.getSalario();
                            this.idJogador = j.getId();
                        } else if (j.getSalario().compareTo(this.Salario) == 0) {
                            if (this.idJogador > j.getId()) {
                                this.idJogador = j.getId();
                            }
                        }
                    }
                });
            }
        });
        if (!this.hasTime) {
            throw new TimeNaoEncontradoException();
        }

        return this.idJogador;
    }

    @Desafio("buscarSalarioDoJogador")
    public BigDecimal buscarSalarioDoJogador(Long idJogador) {
        this.hasJogador = false;
        this.Salario = new BigDecimal(0);
        this.jogador.forEach(j -> {
            if (j.getId().equals(idJogador)) {
                hasJogador = true;
                this.Salario = j.getSalario();
            }
        });
        if (!this.hasJogador) {
            throw new JogadorNaoEncontradoException();
        }

        return this.Salario;
    }

    @Desafio("buscarTopJogadores")
    public List<Long> buscarTopJogadores(Integer top) {
        List<Long> tops = new ArrayList<>();
        List<Integer> hh = new ArrayList<>();
        List<Long> jj = new ArrayList<>();
        contador = 0;

        if (top <= 0) {
            return tops;
        }
        this.jogador.forEach(j -> {
            if (contador.equals(top)) {
                Integer menorH = null;
                int i = -1;
                for (int f = 0; f < top; f++) {
                    if (f == 0) {
                        menorH = hh.get(f);
                        i = f;
                    } else {
                        if (hh.get(f) < menorH) {
                            menorH = hh.get(f);
                            i = f;
                        }
                    }
                }

                if (i >= 0) {
                    if (j.getNivelHabilidade() > hh.get(i)) {
                        hh.add(i, j.getNivelHabilidade());
                        jj.add(i, j.getId());
                    } else if (j.getNivelHabilidade().equals(hh.get(i)) && j.getId() < jj.get(i)) {
                        hh.add(i, j.getNivelHabilidade());
                        jj.add(i, j.getId());
                    }
                }
            } else {
                hh.add(j.getNivelHabilidade());
                jj.add(j.getId());
                contador++;
            }
        });

        Collections.sort(jj);
        return jj;
    }

    @Desafio("buscarCorCamisaTimeDeFora")
    public String buscarCorCamisaTimeDeFora(Long timeDaCasa, Long timeDeFora) {
        this.corCamisaPrincipalCasa = "";
        this.corCamisaPrincipalFora = "";
        this.corCamisaSecundariaFora = "";

        this.hasTime = false;
        this.time.forEach(t -> {
            if (t.getId().equals(timeDaCasa)) {
                this.hasTime = true;
                this.corCamisaPrincipalCasa = t.getCorUniformePrincipal();
            }
        });

        if (!this.hasTime) {
            throw new TimeNaoEncontradoException();
        }

        this.hasTime = false;
        this.time.forEach(t -> {
            if (t.getId().equals(timeDeFora)) {
                this.hasTime = true;
                this.corCamisaPrincipalFora = t.getCorUniformePrincipal();
                this.corCamisaSecundariaFora = t.getCorUniformeSecundario();
            }
        });

        if (!this.hasTime) {
            throw new TimeNaoEncontradoException();
        }

        if (this.corCamisaPrincipalCasa.equalsIgnoreCase(this.corCamisaPrincipalFora)) {
            return this.corCamisaSecundariaFora;
        } else {
            return this.corCamisaPrincipalFora;
        }
    }

}
