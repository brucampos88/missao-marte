package br.com.elo7.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.elo7.banco.Repository;
import br.com.elo7.dominio.Direcao;
import br.com.elo7.dominio.Planalto;
import br.com.elo7.dominio.Posicao;
import br.com.elo7.dominio.Sonda;

public class SondaServiceTest {
	
	@InjectMocks
	private SondaServiceImpl sondaService;
	
	@Mock
	private Repository repository;
	
	@Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void deveMovimentarASonda() {
		Sonda sonda = new Sonda(Posicao.criar(1, 1, Direcao.NORTE), new Planalto(5,5));
		when(repository.buscar(Sonda.class, 1L)).thenReturn(sonda);
		doNothing().when(repository).atualizar(sonda);
		sonda = sondaService.movimentar(1L, "M");
		assertThat(sonda.getPosicao(), is(equalTo(Posicao.criar(1, 2, Direcao.NORTE))));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void deveLancarExcecaoAoMovimentarSondaSeAMesmaNaoExistir() {
		when(repository.buscar(Sonda.class, 1L)).thenReturn(null);
		sondaService.movimentar(1L, "M");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void deveLancarExcecaoAoCadastrarSondaOPlanaltoSeNaoExistir() {
		Sonda sonda = new Sonda(Posicao.criar(1, 1, Direcao.NORTE), new Planalto(5,5));
		when(repository.buscar(Planalto.class, null)).thenReturn(null);
		sondaService.cadastrar(sonda);
	}
	
	@Test
	public void deveCadastarASondaComOsValoresDaPosicaoOriginais() {
		Sonda sonda = new Sonda(Posicao.criar(1, 1, Direcao.NORTE), new Planalto(5,5));
		when(repository.buscar(Planalto.class, null)).thenReturn(new Planalto(5,5));
		doNothing().when(repository).cadastrar(sonda);
		sondaService.cadastrar(sonda);
		assertThat(sonda.getPosicao(), is(equalTo(Posicao.criar(1, 1, Direcao.NORTE))));
	}
	
	@Test
	public void deveCadastarASondaComOsValoresDaPosicaoAjustados() {
		Sonda sonda = new Sonda(Posicao.criar(9, 7, Direcao.NORTE), new Planalto(5,5));
		when(repository.buscar(Planalto.class, null)).thenReturn(new Planalto(5,5));
		doNothing().when(repository).cadastrar(sonda);
		sondaService.cadastrar(sonda);
		assertThat(sonda.getPosicao(), is(equalTo(Posicao.criar(5, 5, Direcao.NORTE))));
	}

}
