import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopLogin } from './shop-login';

describe('ShopLogin', () => {
  let component: ShopLogin;
  let fixture: ComponentFixture<ShopLogin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShopLogin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShopLogin);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
