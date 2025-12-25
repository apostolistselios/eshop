import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopSignup } from './shop-signup';

describe('ShopSignup', () => {
  let component: ShopSignup;
  let fixture: ComponentFixture<ShopSignup>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShopSignup]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShopSignup);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
