import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Injectable({ providedIn: 'root' })
export class ProductSearchParamsService {
  buildSearchParams(
    form: FormGroup,
    page: number,
    size: number,
  ): Record<string, string | number | boolean> {
    const filters = form.getRawValue();

    const params: Record<string, string | number | boolean> = {
      page,
      size,
    };

    Object.entries(filters).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        if (typeof value === 'number' || typeof value === 'string') {
          params[key] = value;
        }
      }
    });

    return params;
  }
}
