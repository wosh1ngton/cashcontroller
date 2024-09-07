
interface MyObject {
    [key: string]: any;
}
export class FuncoesUtil {

    static filtrarValoresNulos(obj: MyObject): MyObject {
        const filteredObj: MyObject = {};
        for (const key in obj) {
            if (obj.hasOwnProperty(key) && obj[key]) {
                filteredObj[key] = obj[key];
            }
        }
        return filteredObj;
    }

}