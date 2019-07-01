import { Moment } from 'moment';
import { IProposal } from 'app/shared/model/proposal.model';

export interface IPost {
  id?: number;
  creationDate?: Moment;
  postName?: string;
  proposals?: IProposal[];
}

export class Post implements IPost {
  constructor(public id?: number, public creationDate?: Moment, public postName?: string, public proposals?: IProposal[]) {}
}
