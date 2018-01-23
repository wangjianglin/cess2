package io.cess.comm.http2;


import io.cess.comm.http2.annotation.HttpPackageMethod;
import io.cess.comm.http2.annotation.HttpPackageReturnType;
import io.cess.comm.http2.annotation.HttpPackageUrl;
import io.cess.comm.http2.annotation.HttpParamName;

/**
 * 
 * @author liuxiang
 * @date 2015年8月6日 上午10:03:18
 *
 */
@HttpPackageUrl("goods/goodslist.action")
@HttpPackageMethod(HttpMethod.POST)
@HttpPackageReturnType(value = PageResult.class, parameterizedType = { GoodsGerneral.class })
public class GoodsPackage extends HttpPackage {
	
	
	@HttpParamName
	private int currentPage;
	@HttpParamName
	private int recommand;


	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}


	public int getRecommand() {
		return recommand;
	}

	public void setRecommand(int recommandType) {
		this.recommand = recommandType;
	}

}
