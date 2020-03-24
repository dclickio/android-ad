# DCLICK Android-Ad
[ ![Download](https://api.bintray.com/packages/dclickio/maven/ads/images/download.svg) ](https://bintray.com/dclickio/maven/ads/_latestVersion)


### DCLICK 적용 가이드
DCLICK SDK 를 사용하여 DCLICK 광고 및 기타 다른 광고 플랫폼 ( 애드몹 등등 ) 과 미디에이션 기능을 사용 하는 방법에 대한 가이드 입니다.

### 사전준비
- [https://www.dclick.io](https://www.dclick.io) 에 접속하여 가입 합니다.
- 앱 -> 앱 등록 을 클릭하여 만드신 앱을 등록 합니다.

### 프로젝트 연동
#### 1. 라이브러리 임포트
- app 레벨 에 존재하는 build.gradle 파일의 dependencies 항목 안에 아래 코드를 입력해 주세요. 
- Grandle Sync를 눌러 라이브러리를 자동으로 임포트 하여 광고를 넣을 수 있는 준비를 완료 하실 수 있습니다.
```gradle
dependencies {
  ...
  implementation 'io.dclick:ads:0.0.2'

  implementation 'com.google.android.gms:play-services-ads:18.3.0' // 애드몹을 사용 하는 경우만 추가
  implementation 'com.facebook.android:audience-network-sdk:5.7.1' // 페이스북을 사용 하는 경우만 추가
  ...
}
```
- 미디에이션을 네트워크 ( 애드몹, 페이스북 ) 가 있는 경우 사용 하는 네트워크의 라이브러리를 위와 같이 임포트 해 주세요.

#### 2. 광고 매니져 초기화
- AdManager 를 아래와 같이 초기화 합니다. 테스트 시에는 추가 설정값을 넣지 않아도 괜찮습니다.
- 운영 시에는 반드시 아래와 같이 각 네트워크의 **API KEY 와 유닛 아이디**를 입력해 주세요.
- initialize 시에 미디에이션 설정값을 가져 옵니다. 따라서 최대한 앱이 시작하자마자 initialize 를 진행해 주세요.
```java
package ...
import ...
import io.dclick.AdManager;

public class MainActivity extends AppCompatActivity {
  protected void onCreate(Bundle savedInstanceState) {
  
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // 광고 매니져 초기화 ( 테스트시 )
    AdManager.initialize(this);

    /*
    // 광고 매니져 초기화 ( 운영시 )
    AdConfig adConfig = new AdConfig();
    adConfig.setApiKey(AdNetwork.Dclick, "디클릭 API KEY");
    adConfig.setApiKey(AdNetwork.Google, "구글 애드몹 API KEY");

    adConfig.setBannerUnitId(AdNetwork.Dclick, "디클릭 유닛 아이디");
    adConfig.setBannerUnitId(AdNetwork.Google, "구글 애드몹 유닛 아이디");

    AdManager.initialize(this, adConfig);
    */

  }
}
```

#### 3-1. 띠 배너 연동
- AdManager.loadBannerAd(activity, container, listener) 함수를 이용해 띠 배너를 노출 합니다.
```java
public class BannerAdsActivity extends Activity {
  private BannerAd bannerAd;
  
  protected void onCreate(Bundle savedInstanceState) {
    // 광고 매니져 초기화 ( 테스트시 )
    AdManager.initialize(this);
    
    ...
    
    // 띠 배너 로딩 시작
    AdManager.loadBannerAd(this, R.id.linearLayout, new BannerAdListener() {
      @Override
      public void onAdSelected(BannerAd bannerAd) {
          // 띠 배너 선택 완료
          BannerAdsActivity.this.bannerAd = bannerAd;
      }

      @Override
      public void onAdLoaded() {
          // 띠 배너 로드 완료
      }

      @Override
      public void onAdFailed() {
          // 띠 배너 로드 실패
      }
    });
  }
  
  @Override
  protected void onDestroy() {
      if (bannerAd != null) bannerAd.onDestroy();
      super.onDestroy();
  }
}
```

#### 3-2. 전면 배너 연동
- AdManager.loadInterstitialAd(activity, listener) 함수를 이용해 전면 배너를 로드 합니다.
- interstitialAd.show() 함수를 이용해 전면 배너를 보여 줍니다.
```java
public class InterstitialAdsActivity extends Activity {
  private InterstitialAd interstitialAd;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_interstitial_ads);

    // 전면 배너 로드 시작
    AdManager.loadInterstitialAd(this, new InterstitialAdListener() {
      @Override
      public void onAdSelected(InterstitialAd interstitialAd) {
          // 전면 배너 선택 완료
          InterstitialAdsActivity.this.interstitialAd = interstitialAd;
      }

      @Override
      public void onAdLoaded() {
          // 전면 배너 로드 완료
      }

      @Override
      public void onAdFailed() {
          // 전면 배너 로드 실패
      }
    });

    findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 이미 로드된 전면 배너를 표시
        // 전면 배너 표시할 시 자동으로 다른 전면 배너를 로딩 합니다.
        if (interstitialAd != null) interstitialAd.show();
      }
    });
  }

  @Override
  protected void onDestroy() {
    if (interstitialAd != null) interstitialAd.onDestroy();
    super.onDestroy();
  }
}
```

#### 3-3. 보상형 광고 연동
- AdManager.loadRewardedAd(activity, listener) 함수를 이용해 보상형 광고를 로드 합니다.
- rewardedAd.show() 함수를 이용해 보상형 광고를 보여 줍니다.
```java
public class RewardedAdsActivity extends Activity {
  private RewardedAd rewardedAd;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rewarded_ads);

    // 보상형 광고 로드 시작
    AdManager.loadRewardedAd(this, new RewardedAdListener() {
      @Override
      public void onAdSelected(RewardedAd rewardedAd) {
        // 보상형 광고 선택 완료
        RewardedAdsActivity.this.rewardedAd = rewardedAd;
      }

      @Override
      public void onAdLoaded() {
        // 보상형 광고 로드 완료
        // 이때부터 광고를 show 할수 있습니다.
      }

      @Override
      public void onAdFailed() {
        // 보상형 광고 로드 실패
      }

      @Override
      public void onAdClosed() {
        // 보상형 광고 닫힘
        // 사용자가 보상을 받지 않고 닫은 경우
      }

      @Override
      public void onRewardEarned() {
        // 사용자가 보상형 광고를 모두 시청하여 보상을 받는 경우
      }
    });

    findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (rewardedAd != null) rewardedAd.show();
      }
    });
  }

  @Override
  protected void onDestroy() {
    if (rewardedAd != null) rewardedAd.onDestroy();
    super.onDestroy();
  }
}

```

### 3-4. 네이티브 광고 연동
- AdManager.loadNativeAd(activity, listener) 함수를 이용해 네이티브 광고를 로드 합니다.
- onAdLoaded(NativeAdData nativeAdData) 함수에서 네이티브 광고를 표시 합니다.
```java

public class NativeAdsActivity extends Activity {
  private NativeAd nativeAd;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_native_ads);

    // 네이티브 광고 로드 시작
    AdManager.loadNativeAd(this, new NativeAdListener() {
      @Override
      public void onAdSelected(NativeAd nativeAd) {
        // 네이티브 광고 선택 완료
        NativeAdsActivity.this.nativeAd = nativeAd;
      }

      @Override
      public void onAdLoaded(NativeAdData nativeAdData) {
        // 네이티브 광고 로드 완료
        // 아래와 같이 광고 데이터를 표기해 주세요
        String title = nativeAdData.getTitle();
        String desc = nativeAdData.getDescription();
        String iconUrl = nativeAdData.getIcon().getUri();
        String imageUrl = nativeAdData.getImages().get(0).getUri();
      }

      @Override
      public void onAdFailed() {
        // 네이티브 광고 로드 실패
      }
    });
  }

  @Override
  protected void onDestroy() {
    if (nativeAd != null) nativeAd.onDestroy();
    super.onDestroy();
  }
}

```
License
-------
[Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html)
