import { Role } from './role.enum';

export interface CurrentUser {
  id: number;
  email: string;
  role: {
    id: string;
    name: Role;
  };
}
