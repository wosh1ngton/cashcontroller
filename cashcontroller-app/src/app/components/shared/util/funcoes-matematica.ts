export const mediaMovel = (arr: any[], janela: number) => {
    if(janela > arr.length || janela <= 0) return false;

    const resultado = [];
    for(let i = 0; i <= arr.length - janela; i++) {
        const fatia = arr.slice(i, i + janela);
        const media = fatia.reduce((acc, val) => acc + val, 0) / janela;
        resultado.push(parseFloat(media.toFixed(2)));
    }
    return resultado;
}