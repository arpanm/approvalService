import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISubRule, getSubRuleIdentifier } from '../sub-rule.model';

export type EntityResponseType = HttpResponse<ISubRule>;
export type EntityArrayResponseType = HttpResponse<ISubRule[]>;

@Injectable({ providedIn: 'root' })
export class SubRuleService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/sub-rules');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(subRule: ISubRule): Observable<EntityResponseType> {
    return this.http.post<ISubRule>(this.resourceUrl, subRule, { observe: 'response' });
  }

  update(subRule: ISubRule): Observable<EntityResponseType> {
    return this.http.put<ISubRule>(`${this.resourceUrl}/${getSubRuleIdentifier(subRule) as number}`, subRule, { observe: 'response' });
  }

  partialUpdate(subRule: ISubRule): Observable<EntityResponseType> {
    return this.http.patch<ISubRule>(`${this.resourceUrl}/${getSubRuleIdentifier(subRule) as number}`, subRule, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISubRule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISubRule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSubRuleToCollectionIfMissing(subRuleCollection: ISubRule[], ...subRulesToCheck: (ISubRule | null | undefined)[]): ISubRule[] {
    const subRules: ISubRule[] = subRulesToCheck.filter(isPresent);
    if (subRules.length > 0) {
      const subRuleCollectionIdentifiers = subRuleCollection.map(subRuleItem => getSubRuleIdentifier(subRuleItem)!);
      const subRulesToAdd = subRules.filter(subRuleItem => {
        const subRuleIdentifier = getSubRuleIdentifier(subRuleItem);
        if (subRuleIdentifier == null || subRuleCollectionIdentifiers.includes(subRuleIdentifier)) {
          return false;
        }
        subRuleCollectionIdentifiers.push(subRuleIdentifier);
        return true;
      });
      return [...subRulesToAdd, ...subRuleCollection];
    }
    return subRuleCollection;
  }
}
